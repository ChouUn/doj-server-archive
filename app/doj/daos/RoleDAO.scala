package doj.daos

import doj.mixins.RoleMixin
import doj.models.Role
import doj.util.MyPostgresProfile
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RoleDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext)
  extends RoleMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def getAll: Future[Seq[Role]] = db.run {
    Roles.result
  }

  def getByIds(ids: Seq[Int]): Future[Seq[Role]] = db.run {
    (Roles filter (_.id inSet ids)).result
  }

  def getById(id: Int): Future[Option[Role]] = db.run {
    (Roles filter (_.id === id)).result.headOption
  }
}
