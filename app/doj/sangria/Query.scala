package doj.sangria

import doj.models.Role
import doj.sangria.Definition.RoleType
import macros.Macros.getArgument
import sangria.relay.{Connection, ConnectionDefinition}

object Query {

  // TODO: macro
  println(getArgument("123"))

  lazy val ConnectionDefinition(_, roleConnection) =
    Connection.definition[MyContext, Connection, Role]("Role", RoleType)
}
