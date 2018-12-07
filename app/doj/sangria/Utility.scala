package doj.sangria

import doj.sangria.Definition._
import sangria.execution.deferred._
import sangria.execution.{ExceptionHandler, HandledException, QueryReducer}
import sangria.marshalling.ResultMarshaller
import sangria.schema.{Field, ListType, ObjectType, _}
import sangria.validation.ValueCoercionViolation

object Utility {

  import doj.sangria.Exception._

  val QueryType = ObjectType("Query",
    fields = fields[MyContext, Unit](
      Field("users", ListType(UserType),
        description = Some("description"),
        resolve = ctx => {
          import ctx.ctx.userDAO._
          getUsers
        },
        complexity = constantComplexity(10)
      )
    ))

  val Mutation = ObjectType("Mutation", fields[MyContext, Unit]())

  val Resolver: DeferredResolver[MyContext] =
    DeferredResolver.fetchers(
      usersFetcher,
      rolesFetcher,
      rolesByUserFetcher,
      usersByRoleFetcher
    )

  val rejectComplexQuery: QueryReducer[MyContext, MyContext] =
    QueryReducer.rejectComplexQueries(300, (_: Double, _: MyContext) => TooComplexQueryException())

  val ErrorHandler = ExceptionHandler {
    case (_: ResultMarshaller, TooComplexQueryException()) => HandledException("Too complex query. Please reduce the field selection.")
    case (_, AuthenticationException(message)) => HandledException(message)
    case (_, AuthorisationException(message)) => HandledException(message)
  }

  val schema = Schema(QueryType, None)

  def constantComplexity[Ctx](complexity: Double) =
    Some((_: Ctx, _: Args, child: Double) => child + complexity)

  case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

}
