package doj.models

import sangria.execution.deferred.HasId

trait Identifiable {
  def id: Int
}

object Identifiable {
  implicit def hasId[T <: Identifiable]: HasId[T, Int] = HasId(_.id)
}
