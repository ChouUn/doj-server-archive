package daos

import javax.inject.{Inject, Singleton}

import mixins.UserMixin
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext)
  extends UserMixin with HasDatabaseConfigProvider[MyPostgresProfile] {

  import profile.api._

  def list(): Future[Seq[User]] = db.run {
    Users.result
  }

}
