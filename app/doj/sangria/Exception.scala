package doj.sangria

object Exception {

  case class TooComplexQueryException() extends Exception

  case class AuthenticationException(message: String) extends Exception(message)

  case class AuthorisationException(message: String) extends Exception(message)

}