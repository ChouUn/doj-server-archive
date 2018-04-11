package macros

import scala.language.experimental.{macros ⇒ `scalac, please just let me do it!`}

object Macros {

  def getArgument(propertyName: String): Integer = macro ArgumentMacro.impl

}
