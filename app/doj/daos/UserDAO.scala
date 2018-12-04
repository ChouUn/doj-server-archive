package doj.daos

import doj.mixins.UserMixin
import doj.models.User
import doj.util.MyPostgresProfile
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext)
  extends UserMixin
    with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def getUsers: Future[Seq[User]] = {
    db.run {
      Users.result
    }
  }

  def getByIds(ids: Seq[Int]): Future[Seq[User]] = {
    db.run {
      Users.filter(_.id inSet ids).result
    }
  }

  def getById(id: Int): Future[Option[User]] = {
    db.run {
      Users.filter(_.id === id).result.headOption
    }
  }

}
