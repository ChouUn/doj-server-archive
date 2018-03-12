package schemas.scalar

import java.time.OffsetDateTime

import sangria.ast
import sangria.schema._
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object DateTime {

  case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

  def parseOffsetDateTime (s: String): Either[DateCoercionViolation.type, OffsetDateTime] = Try ( {
    OffsetDateTime.parse (s)
  }) match {
    case Success (date) => Right (date)
    case Failure (_) => Left (DateCoercionViolation)
  }

  implicit val OffsetDateTimeType = ScalarType[OffsetDateTime] ("DateTime",
    coerceInput = {
      case ast.StringValue (s, _, _, _, _) => parseOffsetDateTime (s)
      case _ => Left (DateCoercionViolation)
    },
    coerceUserInput = {
      case s: String => parseOffsetDateTime (s)
      case _ => Left (DateCoercionViolation)
    },
    coerceOutput = (d, _) => d.toString
  )

}