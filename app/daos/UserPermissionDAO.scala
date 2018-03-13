package daos

import javax.inject.{Inject, Singleton}

import mixins.UserPermissionMixin
import models.UserPermission
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserPermissionDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit ec: ExecutionContext)
  extends UserPermissionMixin with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def list(): Future[Seq[UserPermission]] = db.run {
    UserPermissions.result
  }

}
