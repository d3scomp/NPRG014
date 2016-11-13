package e33

/* Features:
 * - macros
 * - quasiquotes
 * - reify
 */

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Assert {

  def assertEnabled = true

  def assert(cond: Boolean, msg: String): Unit = macro assertImplTwoArgs
  def assert(cond: Boolean): Unit = macro assertImplOneArg

  def assertImplTwoArgs(c: Context)(cond: c.Expr[Boolean], msg: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._

    if (assertEnabled) {
      reify {
        if (! cond.splice) {
          println("Assertion failed: " + msg.splice)
          sys.exit()
        }
      }
    } else {
      reify {}
    }
  }

  def assertImplOneArg(c: Context)(cond: c.Expr[Boolean]): c.Expr[Unit] = {
    import c.universe._

    val msg = c.Expr[String](q"${cond.tree.toString}")

    if (assertEnabled) {
      reify {
        if (! cond.splice) {
          println("Assertion failed: " + msg.splice)
          sys.exit()
        }
      }
    } else {
      reify {}
    }
  }

}