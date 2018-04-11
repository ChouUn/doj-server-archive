package models

import java.time.OffsetDateTime

case class User(id: Int, account: String, email: String, password: String,
                nickname: String, lastLogin: Option[OffsetDateTime] = None, isActive: Boolean,
                createdAt: OffsetDateTime, updatedAt: OffsetDateTime, version: Int)
  extends Identifiable