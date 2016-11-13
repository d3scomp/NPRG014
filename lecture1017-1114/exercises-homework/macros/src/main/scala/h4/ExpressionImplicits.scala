/* Uncomment this to finalize the code

package h4

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

abstract class Expression
case class Var(name: String) extends Expression
case class Number(num: Double) extends Expression
case class BinOp(operator: String, left: Expression, right: Expression) extends Expression

class ExpressionImplicitsImpl(val c: Context) {
  import c.universe._

  // Add necessary definitions here

}

object ExpressionImplicits {
  def expr(exprTree: AnyRef): Expression = macro ExpressionImplicitsImpl.expr
}

*/
