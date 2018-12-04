package doj.mixins

import doj.models.UserPermission
import doj.util.MyPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider

trait UserPermissionMixin extends Object with UserMixin with PermissionMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api._

  val UserPermissions = new TableQuery(tag => new UserPermissionTable(tag))

  class UserPermissionTable(tag: Tag)
    extends Table[UserPermission](tag, "auth_user_permission")
      with RelationTable {

    def * =
      (id, userId, permissionId).mapTo[UserPermission]

    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(permissionId)).shaped.<>(
      { r =>
        import r._
        _1.map(_ => UserPermission.tupled((_1.get, _2.get, _3.get)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def user =
      foreignKey("auth_user_permission_user_id_fk_auth_user_id", userId, Users)(
        _.id,
        onUpdate = ForeignKeyAction.NoAction,
        onDelete = ForeignKeyAction.NoAction
      )

    def permission =
      foreignKey("auth_user_permission_permission_id_fk_auth_permission_id", permissionId, Permissions)(
        _.id,
        onUpdate = ForeignKeyAction.NoAction,
        onDelete = ForeignKeyAction.NoAction
      )

    def user_permission_uniq =
      index("auth_user_permission_user_id_permission_id_uniq", (userId, permissionId), unique = true)

    def userId: Rep[Int] = column[Int]("user_id")

    def permissionId: Rep[Int] = column[Int]("permission_id")

  }

}
