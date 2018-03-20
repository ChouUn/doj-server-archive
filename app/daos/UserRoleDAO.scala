package daos

import javax.inject.{Inject, Singleton}
import mixins.UserRoleMixin
import models.{Role, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.ExecutionContext


@Singleton
class UserRoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                           (implicit ec: ExecutionContext)
  extends UserRoleMixin
    with HasDatabaseConfigProvider[MyPostgresProfile]
    with Runnable {

  import profile.api._

  def getRolesByUserId(userId: Int): Query[RoleTable, Role, Seq] =
    for (ur <- UserRoles if ur.userId === userId;
         r <- Roles if ur.roleId === r.id
    ) yield r

  def getUsersByRoleId(roleId: Int): Query[UserTable, User, Seq] =
    for (ur <- UserRoles if ur.roleId === roleId;
         u <- Users if ur.userId === u.id
    ) yield u

}
