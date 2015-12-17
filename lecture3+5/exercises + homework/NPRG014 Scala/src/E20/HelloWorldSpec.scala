package E20
import org.specs2.mutable._

// Execute the test by launching the class "specs2.run" with parameters "E20.HelloWorldSpec nocolor"
 
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