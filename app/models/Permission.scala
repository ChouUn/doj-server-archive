package models

import java.time.OffsetDateTime


case class Permission(id: Int, name: String, operation: String,
                      createdAt: OffsetDateTime, updatedAt: OffsetDateTime, version: Int)
