package e25

/* Features:
 * - assignment for extractors
 */

abstract class Expr:
  def simplifyUsing(func: PartialFunction[Expr, Expr]) =
    if (func.isDefinedAt(this)) func(this) else this

case class Number(num: Double) extends Expr
case class Var(name: String) extends Expr
case class BinOp(op: String, left: Expr, right: Expr) extends Expr


object ExpressionsPFWithExtractor:

  def main(args: Array[String]): Unit =

    val expr = BinOp("*", Var("x"), Number(1))
    println(expr)

    /* ASSIGNMENT:
     * Add needed extractors, such that the code below compiles and works correctly.

    val sExpr = expr.simplifyUsing {
      case Number(0) + e => e
      case e + Number(0) => e
      case Number(1) * e => e
      case e * Number(1) => e
    }

    println(sExpr)
     */
