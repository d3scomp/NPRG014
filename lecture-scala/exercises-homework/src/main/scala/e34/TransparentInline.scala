package e34

abstract class Shape
abstract class Rectangular extends Shape
abstract class Oval extends Shape
class Rectangle extends Rectangular
class Square extends Rectangular
class Circle extends Oval
class Ellipse extends Oval


object TranparentInlineTest:

  transparent inline def getShape(inline id: Int) =
    inline if id == 0 then
      new Circle
    else
      new Square

  def draw(shape: Rectangular): Unit =
    println("Drawing rectangular shape")

  def draw(shape: Oval): Unit =
    println("Drawing oval shape")

  def draw(shape: Shape): Unit =
    println("Drawing general shape")

  def main(args: Array[String]): Unit =
    val shape = getShape(1)
    draw(shape)

