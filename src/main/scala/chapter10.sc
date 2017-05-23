// # Scala For The Impatient -- Chapter 10
import java.awt.Point
// ## Exercise 1
// > The java.awt.Rectangle class has useful methods translate and grow that are unfortunately absent from classes such as java.awt.geom.Ellipse2D. In Scala, you can fix this problem. Define a trait RectangleLike with concrete methods translate and grow. Provide any abstract methods that you need for the implementation, so that you can mix in the trait like this:
// ```
// val egg = new java.awt.geom.Ellipse2D.Double(5, 10, 20, 30) with RectangleLike egg.translate(10, -10)
// egg.grow(10, 20)
// ``

// Note that:
// * we can use anything other than `this` to get a reference on the object extending `RectangularShape`. Here we chose `self`, but `foo` would also work
// * we return `self`, which is useful in a worksheet or during a test, since we can write `println(egg.translate(x,y))`
trait RectangleLike {
  self: java.awt.geom.RectangularShape =>

  def translate(dx: Int, dy: Int) = {
    self.setFrame(self.getX + dx, self.getY + dy,
      self.getWidth, self.getHeight)
    self
  }

  def grow(byWidth: Int, byHeight: Int) = {
    self.setFrame(self.getX, self.getY,
      self.getWidth + byWidth, self.getHeight + byHeight)
    self
  }

  override def toString = s"RectangleLike[$getX, $getY, $getWidth, $getHeight]"
}


val egg = new java.awt.geom.Ellipse2D.Double(5, 10, 20, 30) with RectangleLike // RectangleLike[5.0, 10.0, 20.0, 30.0]
egg.translate(10, -10) // RectangleLike[15.0, 0.0, 20.0, 30.0]
egg.grow(2, 2)

// RectangleLike[15.0, 0.0, 22.0, 32.0]

// ## Exercise 2
// > Define a class OrderedPoint by mixing scala.math.Ordered[Point] into java.awt.Point. Use lexicographic ordering, i.e. (x, y) < (x’, y’) if x < x’ or x = x’ and y < y’.

class OrderedPoint(x: Int, y: Int) extends java.awt.Point(x, y) with Ordered[Point] {
  override def compare(that: Point): Int = {
    if (getX == that.getX) getY compare that.getY
    else getX compare that.getX
  }
}

new OrderedPoint(1, 2) < new OrderedPoint(1, 3)

// ## Exercise 3

// > Look at the BitSet class, and make a diagram of all its superclasses and traits. Ignore the type parameters (everything inside the [...]). Then give the linearization of the traits.

// TODO

// ## Exercise 4
// > Provide a CryptoLogger class that encrypts the log messages with the Caesar cipher. The key should be 3 by default, but it should be overridable by the user. Provide usage examples with the default key and a key of –3.

trait Logger {
  def log(msg: String) // an abstract method
}

trait ConsoleLogger extends Logger {
  override def log(msg: String) = println("log: " + msg)
}

trait CryptoLogger extends Logger {
  val key = 3 //
  abstract override def log(msg: String) = super.log(msg.map(c => (key + c).toChar ).mkString)
}

val logger1 = new ConsoleLogger with CryptoLogger
logger1.log("hello world")

val logger2 = new ConsoleLogger with CryptoLogger {override val key = 0}
logger2.log("hello world")