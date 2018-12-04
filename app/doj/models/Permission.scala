package doj.models

import java.time.LocalDateTime

case class Permission(id: Int, name: String, operation: String,
                      createdAt: LocalDateTime, updatedAt: LocalDateTime, version: Int)
  extends Identifiable
