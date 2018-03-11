package schemas

import java.time.OffsetDateTime

import daos.UserDAO
import models.UserMixin.User
import sangria.execution.deferred._
import sangria.macros.derive._
import sangria.schema._
import sangria.ast
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object Auth {
  case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

  def parseOffsetDateTime(s: String) = Try({
    OffsetDateTime.parse(s)
  }) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(DateCoercionViolation)
  }

  implicit val OffsetDateTimeType = ScalarType[OffsetDateTime]("OffsetDateTime",
    coerceInput = {
      case ast.StringValue(s, _, _, _, _) => parseOffsetDateTime(s)
      case _ => Left(DateCoercionViolation)
    },
    coerceUserInput = {
      case s: String => parseOffsetDateTime(s)
      case _ => Left(DateCoercionViolation)
    },
    coerceOutput = (d, _) => d
  )

  val UserType: ObjectType[Unit, User] = deriveObjectType()

  val QueryType = ObjectType("Query",
    description = "The root of all... queries",
    fields = fields[UserDAO, Unit](
      Field("allUser", ListType(UserType),
        description = Some("description"),
        resolve = _.ctx.list()
      )
    ))

  val schema = Schema(QueryType)
}