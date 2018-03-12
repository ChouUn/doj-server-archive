package schemas

object SchemaException {

  case class TooComplexQueryException() extends Exception

  case class AuthenticationException(message: String) extends Exception(message)

  case class AuthorisationException(message: String) extends Exception(message)

}