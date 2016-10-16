package e12

trait DefaultUserServiceComponent extends UserServiceComponent {
	this: UserRepositoryComponent =>

	lazy val userService = new DefaultUserService

	class DefaultUserService extends UserService {
		def findAll = userLocator.findAll

		def save(user: User) {
			userUpdater.save(user: User)
		}
	}
}
