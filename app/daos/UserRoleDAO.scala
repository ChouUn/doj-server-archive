package daos

import javax.inject.{Inject, Singleton}

import mixins.UserRoleMixin
import models.{Role, User, UserRole}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserRoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                           (implicit ec: ExecutionContext)
  extends UserRoleMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def all(): Future[Seq[UserRole]] =
    db.run {
      UserRoles.result
    }

  def getByUserId(userId: Int): Future[Seq[UserRole]] =
    db.run {
      UserRoles.filter(_.userId === userId).result
    }

  def getByRoleId(roleId: Int): Future[Seq[UserRole]] =
    db.run {
      UserRoles.filter(_.roleId === roleId).result
    }

  def getRolesByUserId(userId: Int): Future[Seq[Role]] =
    db.run {
      (for (ur <- UserRoles if ur.userId === userId;
            r <- Roles if ur.roleId === r.id
      ) yield (r)).result
    }

  def getUsersByRoleId(roleId: Int): Future[Seq[User]] =
    db.run {
      (for (ur <- UserRoles if ur.roleId === roleId;
            u <- Users if ur.userId === u.id
      ) yield (u)).result
    }

}
