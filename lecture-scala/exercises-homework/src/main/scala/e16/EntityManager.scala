package e16

class EntityManager:
	def query[T](query: String, cls: Class[T]): List[T] = Nil
	def persist(user: User): Unit = {}
