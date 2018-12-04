package doj.sangria

import doj.models._
import sangria.relay.{Connection, ConnectionDefinition}
import macros.Macros.getArgument
import Definition.RoleType
import doj.models.Role

object Query {

  // TODO: macro
  println(getArgument("123"))

  lazy val ConnectionDefinition(_, roleConnection) =
    Connection.definition[MyContext, Connection, Role]("Role", RoleType)
}
