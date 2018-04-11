package daos

import javax.inject.{Inject, Singleton}
import mixins.PermissionMixin
import models.Permission
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.ExecutionContext

@Singleton
class PermissionDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                             (implicit ec: ExecutionContext)
  extends PermissionMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

}
