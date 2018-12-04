package doj.models

import java.time.LocalDateTime

case class Role(id: Int, name: String,
                createdAt: LocalDateTime, updatedAt: LocalDateTime, version: Int)
  extends Identifiable
