package doj.daos

import doj.mixins.PermissionMixin
import doj.models.Permission
import doj.util.MyPostgresProfile
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.ExecutionContext

@Singleton
class PermissionDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                             (implicit ec: ExecutionContext)
  extends PermissionMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

}
