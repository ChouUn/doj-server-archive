package daos

import javax.inject.{Inject, Singleton}

import mixins.RoleMixin
import models.Role
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class RoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext)
  extends RoleMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def all(): Future[Seq[Role]] =
    db.run {
      Roles.result
    }

  def getByIds(ids: Seq[Int]): Future[Seq[Role]] =
    db.run {
      Roles.filter(_.id inSet ids).result
    }

  def getById(id: Int): Future[Option[Role]] =
    db.run {
      Roles.filter(_.id === id).result.headOption
    }

}
