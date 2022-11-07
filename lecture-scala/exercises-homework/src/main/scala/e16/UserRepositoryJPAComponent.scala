package e16

trait UserRepositoryJPAComponent extends UserRepositoryComponent:
	val em: EntityManager

	lazy val userLocator = new UserLocatorJPA(em)
	lazy val userUpdater = new UserUpdaterJPA(em)

	class UserLocatorJPA(val em: EntityManager) extends UserLocator:
		def findAll = em.query("from User", classOf[User])

	class UserUpdaterJPA(val em: EntityManager) extends UserUpdater:
		def save(user: User) = em.persist(user)
