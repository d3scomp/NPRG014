package e16

trait UserServiceComponent:
	def userService: UserService

	trait UserService:
		def findAll: List[User]
		def save(user: User): Unit
