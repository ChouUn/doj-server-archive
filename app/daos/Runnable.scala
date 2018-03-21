package daos

import play.api.db.slick.HasDatabaseConfigProvider
import utils.MyPostgresProfile

import scala.concurrent.Future

trait Runnable {
  self: HasDatabaseConfigProvider[MyPostgresProfile] =>

  import profile.api.{Query, Table, streamableQueryActionExtensionMethods}

  def run[R, E <: Table[R]](entities: Query[E, R, Seq]): Future[Seq[R]] = {
    db.run {
      entities.result
    }
  }

}
