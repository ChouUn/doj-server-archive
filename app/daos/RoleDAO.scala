package daos

import javax.inject.{Inject, Singleton}
import mixins.RoleMixin
import models.Role
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.ExecutionContext


@Singleton
class RoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext)
  extends RoleMixin
    with HasDatabaseConfigProvider[MyPostgresProfile]
    with Runnable {

  import profile.api._

  def getAllRoles(roles: Query[RoleTable, Role, Seq]): Query[RoleTable, Role, Seq] = {
    roles
  }

  def getRolesByIds(roles: Query[RoleTable, Role, Seq], ids: Seq[Int]): Query[RoleTable, Role, Seq] = {
    roles filter (_.id inSet ids)
  }

  def getRoleById(roles: Query[RoleTable, Role, Seq], id: Int): Query[RoleTable, Role, Seq] = {
    roles filter (_.id === id)
  }

}
