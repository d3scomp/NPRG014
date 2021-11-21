package e16

trait UserRepositoryComponent:
	def userLocator : UserLocator
	def userUpdater : UserUpdater

	trait UserLocator:
		def findAll: List[User]

	trait UserUpdater:
		def save(user: User): Unit
