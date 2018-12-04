package doj.sangria.scalars

import java.time.LocalDateTime

import doj.sangria.Utility.DateCoercionViolation
import sangria.ast
import sangria.schema.ScalarType

import scala.util.{Failure, Success, Try}

object DateTimeType {

  def parseDateTime(str: String): Either[DateCoercionViolation.type, LocalDateTime] =
    Try({
      LocalDateTime.parse(str)
    }) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(DateCoercionViolation)
    }

  implicit val DateTimeType: ScalarType[LocalDateTime] = ScalarType[LocalDateTime](
    name = "DateTime",
    description = Some(
      """
        | A date-time without a time-zone in the ISO-8601 calendar system,
        | such as `2007-12-03T10:15:30`.
      """.stripMargin),
    coerceInput = {
      case ast.StringValue(str, _, _, _, _) => parseDateTime(str)
      case _ => Left(DateCoercionViolation)
    },
    coerceUserInput = {
      case str: String => parseDateTime(str)
      case _ => Left(DateCoercionViolation)
    },
    coerceOutput = (value: LocalDateTime, _) => value.toString
  )

}
