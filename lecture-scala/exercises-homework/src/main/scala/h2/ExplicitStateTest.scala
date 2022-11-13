/*
Implement the classes below such that the main (without modifications) prints out the something like this:

Person John Doe aged 24
Person John Doe aged 25
List(h2.PersonState@3d24753a)
Person John Doe aged 24
Thing Box with color (255,0,0)
Person Joe aged 24

*/

/*
package h2

import scala.collection.mutable.ListBuffer


trait WithExplicitState:
  /* add necessary declarations here */

  protected def state: /* add the correct type here */
  protected def state_=(state: /* add the correct type here */): Unit


class PersonState(val name: String, val age: Int)

class Person extends WithExplicitState:
  /* Implement this class. It should have no knowledge of the trait History. It should use instances of PersonState as the state. */


type RGBColor = (Int, Int, Int)
class ThingState(val name: String, val color: RGBColor)

class Thing extends WithExplicitState:
  /* Implement this class. It should have no knowledge of the trait History. It should use instances of ThingState as the state. */



trait History:
    /* Add necessary declarations here. This trait should have no knowledge of classes Person, Thing, PersonState, ThingState.
       It should depend only on the trait WithExplicitState.
    */

    val hist = ListBuffer.empty[/* add the correct type here */]

    def checkpoint(): /* add the correct type here */ =
      hist.append(state)
      this

    def history = hist.toList

    def restoreTo(s: /* add the correct type here */): /* add the correct type here */ =
      state = s
      this


object ExplicitStateTest:
  def main(args: Array[String]): Unit =
    // The inferred type of variable "john" should be "Person & History".
    val john = (new Person with History).setName("John Doe").setAge(24).checkpoint()

    println(john)
    john.setAge(25)

    println(john)
    println(john.history)

    val johnsPrevState = john.history(0)
    john.restoreTo(johnsPrevState)
    println(john)

    // The inferred type of variable "box" should be "Thing & History".
    val box = new Thing with History
    box.setName("Box")
    box.setColor((255, 0, 0))
    println(box)

    val joe = new Person with History
    joe.restoreTo(johnsPrevState).setName("Joe")
    println(joe)

    // The line below must not compile. It should complain about an incompatible type.
    // box.restoreTo(prevState)

*/