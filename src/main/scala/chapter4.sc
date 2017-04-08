// # Scala For The Impatient -- Chapter 4

// ## Exercise 1
// > Set up a map of prices for a number of gizmos that you covet. Then produce a second map with the same keys and the prices at a 10 percent discount.

// Create the map using tuples:
val wishlist1 = Map(("this", 10.0), ("that", 25.0), ("and that", 123.1))

// Create the map using map notation:
val whishlist2 = Map(
  "this" -> 10,
  "that" -> 25,
  "and that" -> 123.1
)

// Discount using map:
wishlist1.mapValues(_ * .9)

// Discount using for:
for ((k, v) <- wishlist1) yield (k, v * .9)


// ## Exercise 2
// > Write a program that reads words from a file. Use a mutable map to count how often each word appears. To read the words, simply use a java.util.Scanner

// create a file in /tmp called quote.txt before running this
// example:
val filename = "/tmp/quote.txt"

// By default, Maps are read only. Here, we import the proper package
// and rename the `Map` to `MutableMap` to avoid confusion
import scala.collection.{JavaConverters, mutable}
import scala.collection.mutable.{Map => MutableMap}

// Using Java classes:
var wordcount1 = MutableMap[String, Int]().withDefault(_ => 0)
val in = new java.util.Scanner(new java.io.File(filename))
while (in.hasNext()) wordcount1(in.next.toLowerCase) += 1
wordcount1

// A more scalarest way:
// Note that the lambda could also be written:
// ```
//  (m, w) => {m(w) += 1; m}
// ```
scala.io.Source.fromFile(filename).mkString
  .split("\\s+")
  .map(_.toLowerCase)
  .foldLeft {
    MutableMap[String, Int]().withDefault(_ => 0)
  } {
    (m, w) => m += (w -> (m(w) + 1))
  }

// ## Exercise 3
// > Repeat the preceding exercise with an immutable map.
//
// Here, the sole change is the `+=` which becomes `+`.
// Also, recall that the `+` on a map override the record if the key
// already exists...
import scala.collection.immutable.{Map => ImutableMap}

scala.io.Source.fromFile(filename).mkString.split("\\s+")
  .map(_.toLowerCase)
  .foldLeft {
    ImutableMap[String, Int]()
  } {
    (m, w) => m + (w -> (m.getOrElse(w, 0) + 1))
  }

// ## Exercise 4
// > Repeat the preceding exercise with a sorted map, so that the words are printed in sorted order.
//
// The only change is the use of `SortedMap` as the `foldLeft` accumulator.
scala.io.Source.fromFile(filename).mkString.split("\\s+")
  .map(_.toLowerCase)
  .foldLeft {
    mutable.SortedMap[String, Int]().withDefault(_ => 0)
  } {
    (m, w) => {m(w) += 1; m}
  }

// ## Exercise 5
// > Repeat the preceding exercise with a java.util.TreeMap that you adapt to the Scala API.
//
// Here, we just show how to get an accumulator of the scala `Map` type. Use it in the code of exercise 4 to test it (the result should not change, since a tree map is a sorted map).

// The first way is to use `mapAsScalaMap`:
import scala.collection.JavaConverters.mapAsScalaMap

var acc1: MutableMap[String, Int] = mapAsScalaMap {
  // Note that even though we use a Java class, we use scala classes as type parameters
  new java.util.TreeMap[String, Int]
}

// We can also use `asScala`:
import scala.collection.JavaConverters._

var acc2: MutableMap[String, Int] =
  new java.util.TreeMap[String, Int].asScala


// ## Exercise 6
// > Define a linked hash map that maps "Monday" to `java.util.Calendar.MONDAY`, and similarly for the other weekdays. Demonstrate that the elements are visited in insertion order.

// ------------------------------
// ### Map digression

// __Populating__ a mutable map

var m1 = new mutable.HashMap[String, Int]
// * using `put`
m1.put("One", 1)
// * using the `+` operator
m1 += ("Two" -> 2)
// * using `update`
m1 update("Three", 3)
// * using an assignment (_Warning_: this fails in an IntelliJ worksheet...):
//m1("Four") = 4

// __creating__ a map

// * using the regular `new` keyword
new mutable.HashMap[String, Int]()
// * using the `object.Map.apply` method
mutable.HashMap("One" -> 1, "Two" -> 2)
// ------------------------------

// create the map. Note that days in `java.util.Calendar` are `int` constants, not enum.
import java.util.Calendar._

var weekdays = new mutable.LinkedHashMap[Int, String]
weekdays += (MONDAY -> "Monday")
weekdays += (TUESDAY -> "Tuesday")
weekdays += (WEDNESDAY -> "Wednesday")
weekdays += (THURSDAY -> "Thursday")
weekdays += (FRIDAY -> "Friday")
weekdays += (SATURDAY -> "Saturday")
weekdays += (SUNDAY -> "Sunday")


// Iterating over the elements follows the insertion order:
for (entry <- weekdays) println(entry)


// One can do a more concise example with:
for ((k, v) <- mutable.LinkedHashMap(
  "Monday" -> MONDAY,
  "Tuesday" -> TUESDAY,
  "Wednesday" -> WEDNESDAY,
  "Thursday" -> THURSDAY,
  "Friday" -> FRIDAY,
  "Saturday" -> SATURDAY,
  "Sunday" -> SUNDAY))
  println(s""""$k\" -> $v""")

// ## Exercise 7
// > Print a table of all Java properties

// First, convert the java properties to a scala map
import scala.collection.JavaConverters._

val props: MutableMap[String, String] = System.getProperties().asScala

// Get the longest key:
val longestKey = props.keys.foldLeft(0) {
  (cnt, k) => if (k.length > cnt) k.length else cnt
}

// Print the table:
//
// the call to `take` limits the value size, since properties can be quite
// long.
//
// to format the string, we first use _string interpolation_ to create the
// formatter (ending with something like `s"%-15s| %s"`, `%s` being a string placeholder
// and `-15` meaning "_a right padding_ of 15"), then we use the `format` method to replace
// the placeholders with actual values.
//
// result:
// ```
// java.runtime.name             | Java(TM) SE Runtime Environment
// java.vm.specification.vendor  | Oracle Corporation
// java.vm.version               | 25.92-b14
// user.country.format           | CH
// gopherProxySet                | false
// java.vm.vendor                | Oracle Corporation
// java.vendor.url               | http://java.oracle.com/
// path.separator                | :
// ...
// ```
for ((k, v) <- props)
  println(s"%-${longestKey + 1}s| %s" format(k, v take 40))


// ## Exercise 8
// > Write a function minmax(values: Array[Int]) that returns a pair containing the smallest and largest values in the array.

// Okay, I am quite fond of the `fold` method...
def minmax(array: Array[Int]) = array.foldLeft((Int.MaxValue, Int.MinValue)) {
  (tup, i) => (tup._1 min i, tup._2 max i)
}


// ## Exercise 9
// > Write a function lteqgt(values: Array[Int], v: Int) that returns a triple containing the counts of values less than v, equal to v, and greater than v.

// Using a foreach:
def lteggt1(values: Array[Int], v: Int) = {
  var (less, eq, high) = (0, 0, 0)

  values.foreach {
    i => if (i < v) less += 1 else if (i == v) eq += 1 else high += 1
  }

  (less, eq, high)
}

// Using (again) a fold:
def lteggt2(values: Array[Int], v: Int) = values.foldLeft((0, 0, 0)) {
  (tup, i) =>
    (
      tup._1 + (if (i < v) 1 else 0),
      tup._2 + (if (i == v) 1 else 0),
      tup._3 + (if (i > v) 1 else 0)
    )
}

// We could simplify the fold by defining two _implicits_ :
{
  // 1. a conversion boolean => int
  implicit def bool2int(b: Boolean) = if (b) 1 else 0

  // 2. the "+" operator between tuple3
  implicit class Tupple3Add(t1: (Int, Int, Int)) {
    def +(t2: (Int, Int, Int)) =
      (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
  }

  // and here we go:
  def lteggt3(values: Array[Int], v: Int) = values.foldLeft((0, 0, 0)) {
    (tup, i) => tup + (i < v, i == v, i > v)
  }

}

val arr = (0 to 10).toArray
lteggt1(arr, 5) //> (Int, Int, Int) = (5,1,5)


// ## Exercise 10
// > What happens when you zip together two strings, such as `"Hello".zip("World")`? Come up with a plausible use case.
"Hello".zip("World") ==
  Vector(('H', 'W'), ('e', 'o'), ('l', 'r'), ('l', 'l'), ('o', 'd'))

// Say we need a mapping between lowercase and uppercase:
//
// (ok, it is completely useless, but hey ! I tried)
val lowerUpper = "abcdefghijklmnopqrstuvwxyz" zip "ABCDEFGHIJKLMNOPQRSTUVWXYZ" toMap