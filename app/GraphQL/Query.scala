package GraphQL

import models._
import sangria.relay.{Connection, ConnectionDefinition}
import macros.Macros.getArgument
import Definition.RoleType

object Query {

  // TODO: macro
  println(getArgument("123"))

  lazy val ConnectionDefinition(_, roleConnection) =
    Connection.definition[MyContext, Connection, Role]("Role", RoleType)
}