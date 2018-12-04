package doj.sangria.scalars

import java.time.OffsetDateTime

import sangria.ast
import sangria.schema._
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object DateTime {

  case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

  def parseOffsetDateTime(s: String): Either[DateCoercionViolation.type, OffsetDateTime] = Try({
    OffsetDateTime.parse(s)
  }) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(DateCoercionViolation)
  }

  implicit val OffsetDateTimeType: ScalarType[OffsetDateTime] = ScalarType[OffsetDateTime](
    name = "DateTime",
    description = Some("""
      | A Datetime with an offset from UTC/Greenwich in the ISO-8601 calendar system,
      | such as "2007-12-03T10:15:30+01:00".
    """.stripMargin),
    coerceInput = {
      case ast.StringValue(s, _, _, _, _) => parseOffsetDateTime(s)
      case _ => Left(DateCoercionViolation)
    },
    coerceUserInput = {
      case s: String => parseOffsetDateTime(s)
      case _ => Left(DateCoercionViolation)
    },
    coerceOutput = (d, _) => d.toString
  )

}