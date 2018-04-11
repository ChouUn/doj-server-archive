package GraphQL

import sangria.execution.deferred._
import sangria.execution.{ExceptionHandler, HandledException, QueryReducer}
import sangria.marshalling.ResultMarshaller
import sangria.schema.{Field, ListType, ObjectType, _}
import GraphQL.Definition._

object Utility {

  import GraphQL.Exception._
  import GraphQL.Query._

  val QueryType = ObjectType("Query",
    description = "The root of all... queries",
    fields = fields[MyContext, Unit](
      Field("allUser", ListType(UserType),
        description = Some("description"),
        complexity = constantComplexity(10),
        resolve = ctx => {
          import ctx.ctx.userDAO._
          getUsers
        }
      )
    ))

  val Mutation = ObjectType("Mutation", fields[MyContext, Unit]())

  val Resolver: DeferredResolver[MyContext] =
    DeferredResolver.fetchers(
      usersFetcher,
      rolesFetcher,
      userRolesFetcher,
    )

  val rejectComplexQuery: QueryReducer[MyContext, MyContext] =
    QueryReducer.rejectComplexQueries(300, (_: Double, _: MyContext) => TooComplexQueryException())

  val ErrorHandler = ExceptionHandler {
    case (_: ResultMarshaller, TooComplexQueryException()) => HandledException("Too complex query. Please reduce the field selection.")
    case (_, AuthenticationException(message)) ⇒ HandledException(message)
    case (_, AuthorisationException(message)) ⇒ HandledException(message)
  }

  val schema = Schema(QueryType, None)

  def constantComplexity[Ctx](complexity: Double) =
    Some((_: Ctx, _: Args, child: Double) => child + complexity)

}
