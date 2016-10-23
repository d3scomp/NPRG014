package e24

import org.specs2.mutable._

// Execute the test by launching the class "specs2.run" with parameters "e20.HelloWorldSpec nocolor"
// Or by sbt "test-only e20.HelloWorldSpec"
class HelloWorldSpec extends Specification {

	"The 'Hello world' string" should {
		"contain 11 characters" in {
			"Hello world" must haveSize(11)
		}
		"start with 'Hello'" in {
			"Hello world" must startWith("Hello")
		}
		"end with 'world'" in {
			"Hello world" must endWith("universe")
		}
	}
}