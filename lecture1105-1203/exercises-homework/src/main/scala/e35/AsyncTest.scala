package e35

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import Async._

object AsyncTest {
  def aInt : Future[Int] = {
    Future {
      println("Processing aInt")
      Thread.sleep(1000)
      42
    }
  }

  def aString(i: Int) : Future[String] = {
    Future {
      println("Processing aString")
      Thread.sleep(1000)
      "Hello " + i
    }
  }

  def aTest(): Future[String] = {
    val ret = Promise[String]()

    Future {
      println("Processing aTest")

      val i_future = aInt
      i_future.onSuccess {
        case i =>
          println(i)
          val a_future = aString(i)
          a_future.onSuccess {
            case s =>
              ret.success{
                println("Finished aTest")
                s
              }
          }
      }
    }

    ret.future
  }

  def aTest2(): Future[String] = async {
    println("Processing aTest2")
    val i = await { aInt }
    println(i)
    val s = await { aString(i) }
    println("Finished aTest2")
    s
  }


  def main(args: Array[String]): Unit = {
    val s = Await.ready(aTest, Duration.Inf)
    println("Result: " + s)

    println()

    val s2 = Await.ready(aTest2, Duration.Inf)
    println("Result: " + s2)
  }
}
