package e12

/* Features:
 * - trait dependency
 * - cake pattern
 */

object Application extends DefaultUserServiceComponent with UserRepositoryJPAComponent {
	val em = new EntityManager

	def main(args: Array[String]) {
		val users = userService.findAll
	}
}
