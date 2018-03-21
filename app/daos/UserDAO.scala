package daos

import javax.inject.{Inject, Singleton}
import mixins.UserMixin
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import utils.MyPostgresProfile

import scala.concurrent.ExecutionContext

@Singleton
class UserDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext)
  extends UserMixin
    with HasDatabaseConfigProvider[MyPostgresProfile]
    with Runnable {

  import profile.api._

  def getAllUsers(users: Query[UserTable, User, Seq]): Query[UserTable, User, Seq] = {
    users
  }

  def getUsersByIds(users: Query[UserTable, User, Seq], ids: Seq[Int]): Query[UserTable, User, Seq] = {
    users filter (_.id inSet ids)
  }

  def getUserById(users: Query[UserTable, User, Seq], id: Int): Query[UserTable, User, Seq] = {
    users filter (_.id === id)
  }

}
