package schemas

import models._
import sangria.macros.derive._
import sangria.relay.{Connection, ConnectionArgs, ConnectionDefinition}
import sangria.schema._
import schemas.scalar.DateTime._

import scala.concurrent.ExecutionContext.Implicits.global

object SchemaQuery {

  lazy val UserType: ObjectType[MyContext, User] = deriveObjectType(
    AddFields(
      Field("roles", roleConnection,
        description = Some(""),
        arguments = Connection.Args.All,
        resolve = ctx => {
          import ctx.ctx.userRoleDAO._
          val rolesQuery = getRolesByUserId(ctx.value.id)
          val rolesFuture = run[Role, RoleTable](rolesQuery)
          Connection.connectionFromFutureSeq(rolesFuture, ConnectionArgs(ctx))
        }),
    )
  )

  lazy val RoleType: ObjectType[MyContext, Role] = deriveObjectType()

  lazy val ConnectionDefinition(_, roleConnection) =
    Connection.definition[MyContext, Connection, Role]("Role", RoleType)
}