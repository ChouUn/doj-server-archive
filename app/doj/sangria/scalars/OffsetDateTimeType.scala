package doj.sangria.scalars

import java.time.OffsetDateTime

import doj.sangria.Utility.DateCoercionViolation
import sangria.ast
import sangria.schema.ScalarType

import scala.util.{Failure, Success, Try}

object OffsetDateTimeType {

  def parseOffsetDateTime(str: String): Either[DateCoercionViolation.type, OffsetDateTime] =
    Try({
      OffsetDateTime.parse(str)
    }) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(DateCoercionViolation)
    }

  implicit val OffsetDateTimeType: ScalarType[OffsetDateTime] = ScalarType[OffsetDateTime](
    name = "OffsetDateTime",
    description = Some(
      """
        | A date-time with an offset from UTC/Greenwich in the ISO-8601 calendar system,
        | such as `2007-12-03T10:15:30+01:00`.
      """.stripMargin),
    coerceInput = {
      case ast.StringValue(str, _, _, _, _) => parseOffsetDateTime(str)
      case _ => Left(DateCoercionViolation)
    },
    coerceUserInput = {
      case str: String => parseOffsetDateTime(str)
      case _ => Left(DateCoercionViolation)
    },
    coerceOutput = (value: OffsetDateTime, _) => value.toString
  )

}