package doj.mixins

import java.time.OffsetDateTime

import doj.models.Role
import doj.util.MyPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult => GR}

trait RoleMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api._

  val Roles = new TableQuery(tag => new RoleTable(tag))

  implicit def GetResultRole(implicit e0: GR[Int], e1: GR[String],
                             e2: GR[OffsetDateTime]): GR[Role] = GR { positionedResult =>
    import positionedResult.<<
    Role.tupled((
      <<[Int], <<[String], <<[OffsetDateTime], <<[OffsetDateTime], <<[Int]
    ))
  }

  class RoleTable(tag: Tag)
    extends Table[Role](tag, "auth_role")
      with EntityTable {

    override def * = (id, name, createdAt, updatedAt, version).mapTo[Role]

    def ? = (
      Rep.Some(id), Rep.Some(name),
      Rep.Some(createdAt), Rep.Some(updatedAt), Rep.Some(version)
    ).shaped.<>(
      { r =>
        import r._
        _1.map(_ => Role.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Length(128, varying = true), O.Unique)

    def createdAt =
      column[OffsetDateTime]("created_at", O.SqlType("TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP"))

    def updatedAt =
      column[OffsetDateTime]("updated_at", O.SqlType("TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP"))

    def version = column[Int]("version", O.Default(1))

    def name_like = index("auth_role_name_like", name)

  }

}