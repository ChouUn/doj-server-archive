package doj.mixins

import java.time.LocalDateTime

import slick.lifted.Rep

trait EntityTable {

  def id: Rep[Int]

  def createdAt: Rep[LocalDateTime]

  def updatedAt: Rep[LocalDateTime]

  def version: Rep[Int]

}
