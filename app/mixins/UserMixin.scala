package mixins

import java.time.OffsetDateTime

import models.User
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult => GR}
import utils.MyPostgresProfile


trait UserMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api._

  implicit def GetResultUser(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[OffsetDateTime]], e3: GR[Boolean],
                             e4: GR[OffsetDateTime]): GR[User] = GR { positionedResult =>
    import positionedResult.{<<, <<?}
    User.tupled((
      <<[Int], <<[String], <<[String], <<[String],
      <<[String], <<?[OffsetDateTime], <<[Boolean],
      <<[OffsetDateTime], <<[OffsetDateTime], <<[Int]
    ))
  }

  val Users = new TableQuery(tag => new UserTable(tag))

  class UserTable(tag: Tag) extends Table[User](tag, "auth_user") with EntityTable {

    override def * = (
      id, account, email, password,
      nickname, lastLogin, isActive,
      createdAt, updatedAt, version
    ) <> (User.tupled, User.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (
      Rep.Some(id), Rep.Some(account), Rep.Some(email), Rep.Some(password),
      Rep.Some(nickname), lastLogin, Rep.Some(isActive),
      Rep.Some(createdAt), Rep.Some(updatedAt), Rep.Some(version)
    ).shaped.<>(
      { r =>
        import r._
        _1.map(_ => User.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def account = column[String]("account", O.Length(128, varying = true), O.Unique)

    def email = column[String]("email", O.Length(254, varying = true), O.Unique)

    def password = column[String]("password", O.Length(256, varying = true))

    def nickname = column[String]("nickname", O.Length(64, varying = true))

    def lastLogin = column[Option[OffsetDateTime]]("last_login", O.Default(None))

    def isActive = column[Boolean]("is_active", O.Default(true))

    def createdAt = column[OffsetDateTime]("created_at", O.SqlType("TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP"))

    def updatedAt = column[OffsetDateTime]("updated_at", O.SqlType("TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP"))

    def version = column[Int]("version", O.Default(1))

    def nickname_like = index("auth_user_nickname_like", nickname)

  }

}
