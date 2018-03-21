package daos

import javax.inject.{Inject, Singleton}
import mixins.RolePermissionMixin
import models.RolePermission
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.ExecutionContext

@Singleton
class RolePermissionDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit ec: ExecutionContext)
  extends RolePermissionMixin with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def getAllRolePermissions(rolePermissions: Query[RoleTable, RolePermission, Seq]
                           ): Query[RoleTable, RolePermission, Seq] = {
    rolePermissions
  }

}
