package e12

/* Features:
 * - mocking
 * - acceptance specification with Specs2
 */

import org.specs2.Specification
import org.specs2.mock.Mockito

// Execute the test by launching the class "specs2.run" with parameters "e12.CakeTest nocolor"
// Or by sbt "test-only e12.CakeTest"
class CakeTest extends Specification with Mockito {
  def is = s2"""
    This is a specification of the DefaultUserServiceComponent.

    findAll method returns all users managed by the user locator    $e1
    findAll method invokes the user locator once                    $e2
    """

  trait UserRepositoryMock extends UserRepositoryComponent {
    val userLocator = mock[UserLocator]
    val userUpdater = null
  }

  object Application extends DefaultUserServiceComponent with UserRepositoryMock

  val users = List(new User("A"), new User("B"))
  Application.userLocator.findAll returns users

  def e1 = Application.userService.findAll must_== users
  def e2 = there was one(Application.userLocator).findAll

  /* ASSIGNMENT:
  Extend the test specification by the following test: "save method delegates to save on user updater"
   */
}