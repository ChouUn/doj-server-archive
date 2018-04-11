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

  def getByIds(ids: Seq[Int]): Future[Seq[UserRole]] =
    db.run {
      (UserRoles filter (_.id inSet ids)).result
    }

  def getByUserIds(userIds: Seq[Int]): Future[Seq[UserRole]] =
    db.run {
      (UserRoles filter (_.userId inSet userIds)).result
    }

  def getByRoleIds(roleIds: Seq[Int]): Future[Seq[UserRole]] =
    db.run {
      (UserRoles filter (_.roleId inSet roleIds)).result
    }

}
