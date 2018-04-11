package mixins

import models.UserRole
import play.api.db.slick.HasDatabaseConfigProvider
import utils.MyPostgresProfile

trait UserRoleMixin extends Object with UserMixin with RoleMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api._

  class UserRoleTable(tag: Tag)
    extends Table[UserRole](tag, "auth_user_role")
      with RelationTable {

    def * = (id, userId, roleId).mapTo[UserRole]

    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(roleId)).shaped.<>(
      { r =>
        import r._
        _1.map(_ => UserRole.tupled((_1.get, _2.get, _3.get)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def userId: Rep[Int] = column[Int]("user_id")

    def roleId: Rep[Int] = column[Int]("role_id")

    def user =
      foreignKey("auth_user_role_user_id_fk_auth_user_id", userId, Users)(
        _.id,
        onUpdate = ForeignKeyAction.NoAction,
        onDelete = ForeignKeyAction.NoAction
      )

    def role =
      foreignKey("auth_user_role_role_id_fk_auth_role_id", roleId, Roles)(
        _.id,
        onUpdate = ForeignKeyAction.NoAction,
        onDelete = ForeignKeyAction.NoAction
      )

    def user_role_uniq =
      index("auth_user_role_user_id_role_id_uniq", (userId, roleId), unique = true)

  }

  val UserRoles = new TableQuery(tag => new UserRoleTable(tag))

}
