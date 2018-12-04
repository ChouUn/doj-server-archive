package doj.mixins

import java.time.LocalDateTime

import doj.models.User
import doj.util.MyPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult => GR}


trait UserMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import MyPostgresProfile.api._

  implicit def GetResultUser(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[LocalDateTime]], e3: GR[Boolean],
                             e4: GR[LocalDateTime]): GR[User] = GR { positionedResult =>
    import positionedResult.{<<, <<?}
    User.tupled((
      <<[Int], <<[String], <<[String], <<[String],
      <<[String], <<?[LocalDateTime], <<[Boolean],
      <<[LocalDateTime], <<[LocalDateTime], <<[Int]
    ))
  }

  val Users = new TableQuery(tag => new UserTable(tag))

  class UserTable(tag: Tag)
    extends Table[User](tag, "auth_user")
      with EntityTable {

    override def * =
      (id, account, email, password, nickname, lastLogin, isActive, createdAt, updatedAt, version).mapTo[User]

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def account = column[String]("account", O.Length(128, varying = true), O.Unique)

    def email = column[String]("email", O.Length(254, varying = true), O.Unique)

    def password = column[String]("password", O.Length(256, varying = true))

    def nickname = column[String]("nickname", O.Length(64, varying = true))

    def lastLogin = column[Option[LocalDateTime]]("last_login", O.Default(None))

    def isActive = column[Boolean]("is_active", O.Default(true))

    def createdAt =
      column[LocalDateTime]("created_at", O.SqlType("TIMESTAMP DEFAULT timezone('utc', now())"))

    def updatedAt =
      column[LocalDateTime]("updated_at", O.SqlType("TIMESTAMP DEFAULT timezone('utc', now())"))

    def version = column[Int]("version", O.Default(1))

    def nickname_like = index("auth_user_nickname_like", nickname)

  }

}
