package doj.mixins

import java.time.LocalDateTime

import doj.models.Permission
import doj.util.MyPostgresProfile
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.{GetResult => GR}

trait PermissionMixin {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import MyPostgresProfile.api._

  implicit def GetResultPermission(implicit e0: GR[Int], e1: GR[String],
                                   e2: GR[LocalDateTime]): GR[Permission] = GR { positionedResult =>
    import positionedResult.<<
    Permission.tupled((
      <<[Int], <<[String], <<[String], <<[LocalDateTime], <<[LocalDateTime], <<[Int]
    ))
  }

  class PermissionTable(tag: Tag)
    extends Table[Permission](tag, "auth_permission")
      with EntityTable {

    override def * =
      (id, entity, operation, createdAt, updatedAt, version).mapTo[Permission]

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def entity = column[String]("entity", O.Length(128, varying = true))

    def operation = column[String]("operation", O.Length(128, varying = true))

    def createdAt =
      column[LocalDateTime]("created_at", O.SqlType("TIMESTAMP DEFAULT timezone('utc', now())"))

    def updatedAt =
      column[LocalDateTime]("updated_at", O.SqlType("TIMESTAMP DEFAULT timezone('utc', now())"))

    def version = column[Int]("version", O.Default(1))

    def entity_index = index("auth_permission_entity_idx", entity)

    def entity_operation_uniq =
      index("auth_permission_entity_operation_uniq", (entity, operation), unique = true)

  }

  val Permissions = new TableQuery(tag => new PermissionTable(tag))

}