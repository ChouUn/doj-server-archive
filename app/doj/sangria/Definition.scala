package doj.sangria

import doj.models.{Identifiable, Role, User}
import doj.models.{Role, User, UserRole}
import sangria.execution.deferred.{Fetcher, HasId, Relation, RelationIds}
import sangria.macros.derive.{AddFields, Interfaces, deriveObjectType}
import sangria.schema.{Field, IntType, InterfaceType, ListType, ObjectType, fields}

object Definition {

  import scalars.OffsetDateTimeType._

  lazy val IdentifiableType: InterfaceType[MyContext, Identifiable] = InterfaceType(
    "Identifiable",
    fields[MyContext, Identifiable](
      Field("id", IntType, resolve = _.value.id)
    )
  )

  implicit lazy val UserType: ObjectType[MyContext, User] = deriveObjectType[MyContext, User](
    Interfaces[MyContext, User](IdentifiableType),
    AddFields(
      Field("roles", ListType(RoleType),
        resolve = ctx => {
          rolesByUserFetcher.deferRelSeq(rolesByUserRel, ctx.value.id)
        }
      )
    ),
  )

  implicit lazy val RoleType: ObjectType[MyContext, Role] = deriveObjectType[MyContext, Role](
    Interfaces[MyContext, Role](IdentifiableType),
    AddFields(
      Field("users", ListType(UserType),
        resolve = ctx => {
          usersByRoleFetcher.deferRelSeq(usersByRoleRel, ctx.value.id)
        }
      )
    ),
  )

  val usersByRoleRel: Relation[User, (Seq[UserRole], User), Int] =
    Relation[User, (Seq[UserRole], User), Int]("usersByRole", _._1.map(_.roleId), _._2)

  val rolesByUserRel: Relation[Role, (Seq[UserRole], Role), Int] =
    Relation[Role, (Seq[UserRole], Role), Int]("rolesByUser", _._1.map(_.userId), _._2)

  val usersByRoleFetcher: Fetcher[MyContext, User, (Seq[UserRole], User), Int] =
    Fetcher.relOnly((ctx: MyContext, ids: RelationIds[User]) => ctx.userRoleDAO.getUsersByRoleIds(ids(usersByRoleRel)))

  val rolesByUserFetcher: Fetcher[MyContext, Role, (Seq[UserRole], Role), Int] =
    Fetcher.relOnly((ctx: MyContext, ids: RelationIds[Role]) => ctx.userRoleDAO.getRolesByUserIds(ids(rolesByUserRel)))

  val usersFetcher: Fetcher[MyContext, User, User, Int] =
    Fetcher((ctx: MyContext, ids: Seq[Int]) => ctx.userDAO.getByIds(ids))(HasId(_.id))

  val rolesFetcher: Fetcher[MyContext, Role, Role, Int] =
    Fetcher((ctx: MyContext, ids: Seq[Int]) => ctx.roleDAO.getByIds(ids))(HasId(_.id))

}
