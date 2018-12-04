package doj.mixins

import java.time.OffsetDateTime

import slick.lifted.Rep

trait EntityTable {

  def id: Rep[Int]

  def createdAt: Rep[OffsetDateTime]

  def updatedAt: Rep[OffsetDateTime]

  def version: Rep[Int]

}
