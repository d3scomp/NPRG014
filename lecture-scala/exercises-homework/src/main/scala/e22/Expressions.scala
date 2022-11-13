package e22

abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

object Expressions:

	def simplify(expr: Expr): Expr = expr match
		case UnOp("-", UnOp("-", e)) => simplify(e) // Double negation

		case UnOp("-", e) => simplify(e) match
			case Number(eNum) => Number(-eNum)
			case eSimpl => UnOp("-", eSimpl)

		case BinOp("+", e, f) => (simplify(e), simplify(f)) match
			case (Number(0), fSimpl) => fSimpl   // Adding zero
			case (eSimpl, Number(0)) => eSimpl   // Adding zero
			case (eSimpl, fSimpl) => BinOp("+", eSimpl, fSimpl)

		case BinOp("*", e, f) => (simplify(e), simplify(f)) match
			case (Number(1), fSimpl) => fSimpl   // Adding zero
			case (eSimpl, Number(1)) => eSimpl   // Adding zero
			case (eSimpl, fSimpl) => BinOp("*", eSimpl, fSimpl)

		case _ => expr

	def main(args: Array[String]): Unit =

		val expr = BinOp("*", Var("x"), BinOp("*", BinOp("+", Number(1), Number(0)), Number(1)))
		println(expr)
		println(simplify(expr))
		
		/* ASSIGNMENT:
		 * Enhance the simplify method in such a way that it can evaluate operations on numbers.
		 * E.g. it should reduce BinOp("*", Number(2), Number(3)) to Number(6).
		 * After this enhancement, the statement below should produce BinOp("*", Var(x), Number(2))

		val expr2 = BinOp("*", Var("x"), BinOp("*", BinOp("+", Number(1), Number(1)), Number(1)))
		println(expr2)
		println(simplify(expr2))

		 */
