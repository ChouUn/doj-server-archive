package GraphQL

//import GraphQL.MyFetcher.getRolesByUserIds
import models.{Identifiable, Role, User, UserRole}
import sangria.execution.deferred.{Fetcher, HasId, Relation, RelationIds}
import sangria.macros.derive.{AddFields, Interfaces, deriveObjectType}
import sangria.schema.{DeferredValue, Field, IntType, InterfaceType, ListType, ObjectType, fields}
import scalars.DateTime._

import scala.concurrent.Future

object Definition {

  val IdentifiableType: InterfaceType[MyContext, Identifiable] = InterfaceType(
    "Identifiable",
    fields[MyContext, Identifiable](
      Field("id", IntType, resolve = _.value.id)
    )
  )

  implicit lazy val UserType: ObjectType[MyContext, User] = deriveObjectType[MyContext, User](
    Interfaces[MyContext, User](IdentifiableType),
    AddFields(
      Field("userRoles", ListType(UserRoleType),
        resolve = ctx => {
          userRolesFetcher.deferRelSeq(userRoleByUserRel, ctx.value.id)
        }
      )
    )
  )

  implicit  lazy val RoleType: ObjectType[MyContext, Role] = deriveObjectType[MyContext, Role](
    Interfaces[MyContext, Role](IdentifiableType)
  )

  implicit lazy val UserRoleType: ObjectType[MyContext, UserRole] = deriveObjectType[MyContext, UserRole](
    Interfaces[MyContext, UserRole](IdentifiableType)
  )

  val userRoleByUserRel =
    Relation[UserRole, Int]("byUser", v => Seq(v.userId))

  val userRoleByRoleRel =
    Relation[UserRole, Int]("byRole", v => Seq(v.roleId))

  val usersFetcher = Fetcher(
    (ctx: MyContext, ids: Seq[Int]) => ctx.userDAO.getByIds(ids)
  )(HasId(_.id))

  val rolesFetcher = Fetcher(
    (ctx: MyContext, ids: Seq[Int]) => ctx.roleDAO.getByIds(ids)
  )(HasId(_.id))

  val userRolesFetcher = Fetcher.rel(
    (ctx: MyContext, ids: Seq[Int]) => ctx.userRoleDAO.getByIds(ids),
    (ctx: MyContext, ids: RelationIds[UserRole]) => ctx.userRoleDAO.getByUserIds(ids(userRoleByUserRel))
  )(HasId(_.id))

}
