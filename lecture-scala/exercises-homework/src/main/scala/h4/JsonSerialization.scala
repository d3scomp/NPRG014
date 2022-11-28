package h4

// Similar to e33, implement a simple JSON serializer using type-classes
// Make sure the statements in the main can be executed. The sample output is given in comments. You can do the indentation
// as you like.

class PhoneNo(val prefix: Int, val number: Int)
class Person(val firstName: String, val lastName: String, val phone: PhoneNo)
class Address(val person: Person, val street: String, val city: String)

// ... add the necessary classes

/*
object JsonSerializerTest:
  def main(args: Array[String]): Unit =
    import JsonSerializer.given
    val a1 = "Hello"
    println(a1.toJson) // "Hello"

    val a2 = 12
    println(a2.toJson) // 12

    val b1 = List("ab", "cd")
    val b2 = List("ef", "gh")
    println(b1.toJson) // [ "ab", "cd" ]

    val c1 = List(b1, b2)
    println(c1.toJson) // [ [ "ab", "cd" ], [ "ef", "gh" ] ]

    val c2 = Map("b1" -> b1, "b2" -> b2)
    println(c2.toJson) // { "b1": [ "ab", "cd" ], "b2": [ "ef", "gh" ] }

    val d1 = Person("John", "Doe", PhoneNo(1, 123456))
    val d2 = Person("Jane", "X", PhoneNo(420, 345678))
    println(d1.toJson) // { "firstName": "John", "lastName": "Doe", "phone": { "prefix": 1, "number": 123456 } }

    val e1 = Address(d1, "Bugmore Lane 3", "Lerfourche")
    val e2 = Address(d2, "West End Woods 1", "Holmefefer")

    val f = List(e1, e2)
    println(f.toJson) // [ { "person": { "firstName": "John", "lastName": "Doe", "phone": { "prefix": 1, "number": 123456 } }, "street": "Bugmore Lane 3", "city": "Lerfourche" }, { "person": { "firstName": "Jane", "lastName": "X", "phone": { "prefix": 420, "number": 345678 } }, "street": "West End Woods 1", "city": "Holmefefer" } ]

*/