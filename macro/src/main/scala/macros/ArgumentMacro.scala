package macros

import scala.reflect.macros.blackbox

object ArgumentMacro {

  def impl(c: blackbox.Context)(propertyName: c.Expr[String]): c.Expr[Integer] = {
    import c.universe._

    val Literal(Constant(propertyNameConst: String)) = propertyName.tree

    reify {
      println(propertyNameConst)
    }

    c.Expr[Integer](Literal(Constant(propertyNameConst.length)))
  }

}
