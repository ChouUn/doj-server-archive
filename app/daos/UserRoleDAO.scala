package daos

import javax.inject.{Inject, Singleton}

import mixins.UserRoleMixin
import models.UserRole
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserRoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                           (implicit ec: ExecutionContext)
  extends UserRoleMixin with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def list(): Future[Seq[UserRole]] = db.run {
    UserRoles.result
  }

}
