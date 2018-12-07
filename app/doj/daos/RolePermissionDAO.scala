package doj.daos

import doj.mixins.RolePermissionMixin
import doj.util.MyPostgresProfile
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.ExecutionContext

@Singleton
class RolePermissionDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit ec: ExecutionContext)
  extends RolePermissionMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

}
