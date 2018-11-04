package e31

object XMLTest extends App {

	val name = "world"
	val xml = <p>Hello <span class="name">{name}</span>!</p>
	
	println(xml)
	
	val names = for {
		span <- xml \ "span"
		body <- span.child
		if span.attribute("class").getOrElse("").toString() == "name"
	} yield body

	println(names)
}