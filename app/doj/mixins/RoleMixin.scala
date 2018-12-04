package doj.mixins

import java.time.LocalDateTime

import doj.models.Role
import doj.util.MyPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult => GR}

trait RoleMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import MyPostgresProfile.api._

  val Roles = new TableQuery(tag => new RoleTable(tag))

  implicit def GetResultRole(implicit e0: GR[Int], e1: GR[String],
                             e2: GR[LocalDateTime]): GR[Role] = GR { positionedResult =>
    import positionedResult.<<
    Role.tupled((
      <<[Int], <<[String], <<[LocalDateTime], <<[LocalDateTime], <<[Int]
    ))
  }

  class RoleTable(tag: Tag)
    extends Table[Role](tag, "auth_role")
      with EntityTable {

    override def * = (id, name, createdAt, updatedAt, version).mapTo[Role]

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Length(128, varying = true), O.Unique)

    def createdAt =
      column[LocalDateTime]("created_at", O.SqlType("TIMESTAMP DEFAULT timezone('utc', now())"))

    def updatedAt =
      column[LocalDateTime]("updated_at", O.SqlType("TIMESTAMP DEFAULT timezone('utc', now())"))

    def version = column[Int]("version", O.Default(1))

  }

}