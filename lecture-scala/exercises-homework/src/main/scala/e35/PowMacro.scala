package e35

import scala.quoted.{Expr, Quotes}

object PowMacro:

  def pow(x: Double, n: Int): Double =
    if n == 0 then 1 else x * pow(x, n - 1)

  def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
    val value: Double = pow(x.valueOrError, n.valueOrError)
    Expr(value)

