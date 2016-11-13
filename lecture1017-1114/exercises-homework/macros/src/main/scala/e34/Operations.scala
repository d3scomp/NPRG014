package e34

/* Features:
 * - expression tree constructors / extractors
 */

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Operations {

  def increment(variable: Int): Unit = macro incrementImpl

  def incrementImpl(c: Context)(variable: c.Expr[Int]): c.Expr[Unit] = {
    import c.universe._

    println(showRaw(variable))

    variable.tree match {
      case varTree @ Ident(TermName(_)) => c.Expr(q"""$varTree = $varTree + 1""")
      case stat => throw new UnsupportedOperationException("unsupported param of increment: " + showRaw(stat))
    }
  }

  // Add definition of swap here

}