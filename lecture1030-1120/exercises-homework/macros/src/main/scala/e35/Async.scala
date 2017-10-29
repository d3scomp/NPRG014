package e35

/* Features:
 * - expression tree constructors / extractors
 * - .. expansion of list of trees
 * - futures and promises
 */

import scala.collection.mutable
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context
import scala.concurrent._


/*
Implements the async macro.
Note that it works only for await statements only on the top-level inside val.

It does the following translation:

async {
  println("A")
  val i = await { aInt }
  println(i)
  val s = await { aString(i) }
  println(s)
}

---------------------------------------------

val ret = Promise[Unit]()

Future {
  println("A")

  val i_future = aInt
  i_future.onSuccess {
    case i =>
      println(i)
      val a_future = aString(i)
      a_future.onSuccess {
        case s =>
          println(s)
          ret.success()
      }
  }
}

ret.future
*/

class AsyncImpl(val c: Context) {
  import c.universe._


  // When the macro is processed, the scopes for Idents are already resolved. However, we change the declaration
  // of the variables assigned by await. Thus we have to recreate the Idents, to force them to be again resolved.
  private def releaseIdentsInList(list: List[Tree]): List[Tree] = {
    for (tree <- list) yield releaseIdentsInTree(tree)
  }

  private def releaseIdentsInTree(tree: Tree): Tree = {
    tree match {
      case Block(stats, expr) => Block(releaseIdentsInList(stats), releaseIdentsInTree(expr))
      case Apply(fun, args) => Apply(fun, releaseIdentsInList(args))
      case Ident(name) => Ident(name)
      case ValDef(mods, name, tpt, rhs) => ValDef(mods, name, tpt, releaseIdentsInTree(rhs))
      case other => other
    }
  }


  private case class CFGNode(statements: List[Tree], futureStatement: Tree, futureValueVar: TermName, var next: CFGNode = null)

  // Builds a control flow graph. Each node of the graph constitutes a list of statements and a statement that creates
  // a future. The children nodes may execute only after their predecessor's future has completed. Only the leaf node
  // does not create a future. Note that no branching and scoping is assumed in this limited version.
  private def buildCFG[T](expr: Expr[T]): CFGNode = {
    val statsOrig = expr match {
      case Expr(Block(statsList, retExpr)) => statsList :+ retExpr  // Block keeps the last statement separately as it constitutes the return value
      case Expr(stat) => List(stat) // No block is generated if the async hold a single statement
      case unsupportedStat => throw new UnsupportedOperationException("unsupported param of async: " + showRaw(unsupportedStat))
    }

    val stats = releaseIdentsInList(statsOrig)

    var rootCfgNode: CFGNode = null
    var previousCFGNode: CFGNode = null
    val statsBuffer = mutable.ListBuffer.empty[Tree]

    def createCFGNode(futureStatement: Tree): Unit = {
      val futureValueVar = internal.reificationSupport.freshTermName("__await_var_$")

      val cfgNode = new CFGNode(statsBuffer.toList, futureStatement, futureValueVar)

      if (rootCfgNode == null) {
        rootCfgNode = cfgNode
      }

      if (previousCFGNode != null) {
        previousCFGNode.next = cfgNode
      }

      previousCFGNode = cfgNode

      statsBuffer.clear()
    }


    for (statTree <- stats) {
      statTree match {
        case ValDef(modifiers, termName, typeTree, Apply(TypeApply(Select(Ident(TermName("Async")), TermName("await")), typeApplyArgs), List(awaitArgs))) =>
          createCFGNode(awaitArgs)
          statsBuffer += ValDef(modifiers, termName, typeTree, Ident(previousCFGNode.futureValueVar))
        case stat =>
          statsBuffer += stat
      }
    }

    createCFGNode(null)

    rootCfgNode
  }

  def translateCFGNode(node: CFGNode): c.Tree =
    if (node.next != null) {
      val futureVar = internal.reificationSupport.freshTermName("__await_future_$")

      q"""
        {
          ..${node.statements}
          val ${futureVar} = ${node.futureStatement}

          ${futureVar}.onComplete {
            case scala.util.Success(${node.futureValueVar}) => ${translateCFGNode(node.next)}
            case scala.util.Failure(t) => __async_ret.failure(t)
          }
        }
      """
    } else {
      q"""
        __async_ret.success {
         ..${node.statements}
        }
      """
    }

  def async[T: c.WeakTypeTag](expr: c.Expr[T]): c.Expr[Future[T]] = {

    val rootCFGNode = buildCFG(expr)

    val tree = reify {
      val __async_ret = Promise[T]()

      c.Expr(translateCFGNode(rootCFGNode)).splice

      __async_ret.future
    }

//    println(showRaw(expr))
//    println(showRaw(tree))
    println("--- transforming from ---\n" + expr + "\n-------------- to ---------------\n" + tree)

    tree
  }
}

object Async {

  def async[T](expr: T): Future[T] = macro AsyncImpl.async[T]

  def await[T](expr: Future[T]): T = throw new UnsupportedOperationException("await can be used only withing async {} block")

/*
  def main(args: Array[String]): Unit = {
    import scala.reflect.runtime.universe._

    println(showRaw(
      q"""
        println("1")
      """
    ))
  }
*/
}