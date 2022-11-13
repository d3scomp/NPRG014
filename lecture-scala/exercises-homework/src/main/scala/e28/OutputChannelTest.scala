package e28

/* Features:
 * - contravariance
 */
trait OutputChannel[-T]:
	def write(x: T): Unit


class A(val x: Int)
class AA(x: Int) extends A(x)
class AB(x: Int) extends A(x)

object AAOutputChannel extends OutputChannel[AA]:
	def write(x: AA) =
		println("AA: " + x)


object ABOutputChannel extends OutputChannel[AB]:
	def write(x: AB) =
		println("AB: " + x)


object AOutputChannel extends OutputChannel[A]:
	def write(x: A) =
		println("A: " + x)


object OutputChannelTest:
	def main(args: Array[String]): Unit =
		var out: OutputChannel[AA] = null
		
		out = AAOutputChannel
		
		out = AOutputChannel
		
		// out = ABOutputChannel  --  does not compile
