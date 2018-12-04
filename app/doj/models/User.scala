package doj.models

import java.time.LocalDateTime

case class User(id: Int, account: String, email: String, password: String,
                nickname: String, lastLogin: Option[LocalDateTime] = None, isActive: Boolean,
                createdAt: LocalDateTime, updatedAt: LocalDateTime, version: Int)
  extends Identifiable
