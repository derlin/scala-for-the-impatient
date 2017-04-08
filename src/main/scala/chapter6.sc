// # Scala For The Impatient -- Chapter 6

// ## Exercise 1
// > Write an object Conversions with methods inchesToCentimeters, gallonsToLiters, and milesToKilometers.

object Conversions {

  def inchesToCentimeters = 2.54 * (_: Double)

  def gallonsToLiters = 3.78541 * (_: Double)

  def milesToKilometers = 1.60934 * (_: Double)
}

// ## Exercise 2
// > The preceding problem wasn’t very object-oriented. Provide a general super- class UnitConversion and define objects InchesToCentimeters, GallonsToLiters, and MilesToKilometers that extend it.

abstract class UnitConversion(conversionFactor: Double) {
  def convert(d: Double): Double = d * conversionFactor
}

object InchesToCentimeters extends UnitConversion(2.54) {}

object GallonsToLiters extends UnitConversion(3.78541) {}

object MilesToKilometers extends UnitConversion(1.60934) {}


// ## Exercise 3
// > Define an Origin object that extends java.awt.Point. Why is this not actually a good idea? (Have a close look at the methods of the Point class.)

// Why is it bad ? Hum.
//
// * Many of the methods in class Point take a Point2D as a parameter ?
// * a Point is something that moves and changes, while an origin should have readonly coordinates ?
{
  class Origin extends java.awt.Point {}
}

// ## Exercise 4
// > Define a Point class with a companion object so that you can construct Point instances as Point(3, 4), without using new.

{
  // One way to go is to define `apply` method of the object companion:
  object Point {
    def apply(x: Int, y: Int) = new Point(x, y)
  }

  // (in the IntelliJ scala worksheet, the compilation fails if
  // I declare the Point class before its object companion...)
  class Point(x: Int = 0, y: Int = 0) {}


  val p = Point(1, 1)
  println(p) //> A$A0$A$A0$Point$7@482b272
}

// We can also make our `Point` class a _case class_.
// Case classes are just regular classes that are:
// 1. Immutable by default
// 2. Decomposable through pattern matching
// 3. Compared by structural equality instead of by reference
// 4. Succinct to instantiate and operate on

{
  case class Point(x: Int = 0, y: Int = 0) {}
  val p = Point(1, 1)
  println(p) //> Point(1,1)
}


// ## Exercise 5
// > Write a Scala application, using the App trait, that prints the command-line arguments in reverse order, separated by spaces. For example, scala Reverse Hello World should print World Hello.

object Reverse extends App {
  println(args.reverse.mkString(" "))
}

// ## Exercise 6
// > Write an enumeration describing the four playing card suits so that the toString method returns ♣, ♦, ♥, or ♠.

object CardSuit extends Enumeration {
  // Add a type alias to the CardSuit.Value so that you
  // can use `CardSuit` instead of `CardSuit.Value` in parameters type:
  type CardSuit = Value
  val Hearts = Value("♥")
  val Diamonds = Value("♦")
  val Clubs = Value("♣")
  val Spades = Value("♠")
}

println(CardSuit.Hearts) //> ♥


// ## Exercise 7
// > Implement a function that checks whether a card suit value from the preceding exercise is red.


// Without import, the parameter type would by `CardSuit.CardSuit`.
// Without type alias, the parameter type would by `CardSuit.Value`.
import CardSuit._

def isRed(suit: CardSuit): Boolean =
  suit == Hearts || suit == Diamonds

println(isRed(Spades))

//> false

// ## Exercise 8
// > Write an enumeration describing the eight corners of the RGB color cube. As IDs, use the color values (for example, 0xff0000 for Red).

object RGBColors extends Enumeration {
  type RGBColors = Value
  val Black = Value(0x000000)
  val White = Value(0xffffff)
  val Red = Value(0xff0000)
  val Green = Value(0x00ff00)
  val Blue = Value(0x0000ff)
  val Yellow = Value(0xffff00)
  val Cyan = Value(0x00ffff)
  val Magenta = Value(0xff00ff)
}

println(RGBColors.Black) //> Black
println("0x%x".format(RGBColors.Red.id)) //> 0xff0000
