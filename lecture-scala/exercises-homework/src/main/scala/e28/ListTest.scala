package e28

import java.io.File

/* Features
 * - covariance
 */

abstract class MyList[+T]:
  def head: T
  def tail: MyList[T]

class MyListItem[T](val head: T, val tail: MyList[T]) extends MyList[T]

object MyNil extends MyList[Nothing]:
  def head = throw Exception()
  def tail = throw Exception()


object ListTest:
  def main(args: Array[String]): Unit =
    val list = MyListItem("aa", MyListItem(1, MyNil))
