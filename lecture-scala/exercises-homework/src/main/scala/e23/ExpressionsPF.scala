package e23

/* Features:
 * - case sequences
 * - partial functions
 */

class Expr:
	def simplifyUsing(func: PartialFunction[Expr, Expr]) =
		if func.isDefinedAt(this) then func(this) else this


case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

object ExpressionsPF:
	
	def main(args: Array[String]): Unit =

		val expr = BinOp("*", Var("x"), Number(1))
		val sExpr = expr.simplifyUsing {
			case BinOp("+", Number(0), e) => e
			case BinOp("+", e, Number(0)) => e
			case BinOp("*", Number(1), e) => e
			case BinOp("*", e, Number(1)) => e			
		}
		
		println(expr)
		println(sExpr)
