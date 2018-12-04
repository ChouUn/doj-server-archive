package doj.mixins

import java.time.OffsetDateTime

import doj.models.Permission
import doj.util.MyPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult => GR}

trait PermissionMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api._

  implicit def GetResultPermission(implicit e0: GR[Int], e1: GR[String],
                                   e2: GR[OffsetDateTime]): GR[Permission] = GR { positionedResult =>
    import positionedResult.<<
    Permission.tupled((
      <<[Int], <<[String], <<[String], <<[OffsetDateTime], <<[OffsetDateTime], <<[Int]
    ))
  }

  class PermissionTable(tag: Tag)
    extends Table[Permission](tag, "auth_permission")
      with EntityTable {

    override def * =
      (id, entity, operation, createdAt, updatedAt, version).mapTo[Permission]

    def ? = (
      Rep.Some(id), Rep.Some(entity), Rep.Some(operation),
      Rep.Some(createdAt), Rep.Some(updatedAt), Rep.Some(version)
    ).shaped.<>(
      { r =>
        import r._
        _1.map(_ => Permission.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def entity = column[String]("entity", O.Length(128, varying = true))

    def operation = column[String]("operation", O.Length(128, varying = true))

    def createdAt =
      column[OffsetDateTime]("created_at", O.SqlType("TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP"))

    def updatedAt =
      column[OffsetDateTime]("updated_at", O.SqlType("TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP"))

    def version = column[Int]("version", O.Default(1))

    def entity_index = index("auth_permission_entity_idx", entity)

    def entity_operation_uniq =
      index("auth_permission_entity_operation_uniq", (entity, operation), unique = true)

  }

  val Permissions = new TableQuery(tag => new PermissionTable(tag))

}