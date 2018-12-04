package doj.daos

import doj.mixins.UserPermissionMixin
import doj.models.UserPermission
import doj.util.MyPostgresProfile
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.ExecutionContext

@Singleton
class UserPermissionDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit ec: ExecutionContext)
  extends UserPermissionMixin
     with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

}
