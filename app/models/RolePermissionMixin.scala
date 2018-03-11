package models

import play.api.db.slick.HasDatabaseConfigProvider
import utils.MyPostgresProfile

trait RolePermissionMixin extends Object with RoleMixin with PermissionMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api._

  case class RolePermission(id: Int, roleId: Int, permissionId: Int)

  class RolePermissionTable(tag: Tag) extends Table[RolePermission](tag, "auth_role_permission") with RelationTable {

    def * = (id, roleId, permissionId) <> (RolePermission.tupled, RolePermission.unapply)

    def ? = (Rep.Some(id), Rep.Some(roleId), Rep.Some(permissionId)).shaped.<>(
      { r =>
        import r._
        _1.map(_ => RolePermission.tupled((_1.get, _2.get, _3.get)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    def id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def roleId: Rep[Int] = column[Int]("role_id")

    def permissionId: Rep[Int] = column[Int]("permission_id")

    def role = foreignKey("auth_role_permission_role_id_fk_auth_role_id", roleId, Roles)(
      _.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )

    def permission = foreignKey("auth_role_permission_permission_id_fk_auth_permission_id", permissionId, Permissions)(
      _.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )

    def role_permission_uniq = index("auth_role_permission_role_id_permission_id_uniq", (roleId, permissionId), unique = true)

  }

  val RolePermissions = new TableQuery(tag => new RolePermissionTable(tag))

}
