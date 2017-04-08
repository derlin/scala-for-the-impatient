// # Scala For The Impatient -- Chapter 7

// ## Exercise 1
// > Write an example program to demonstrate that
// ```
// package com.horstmann.impatient
// ```
// is not the same as
// ```
// package com
// package horstmann
// package impatient
// ```

package com {

  object ComConstants {def name = "com"}

  package horstmann {

    object HorstmannConstants {def name = "horstmann"}

  }

}

// Here, we _need_ to import the `com` and `com.horstmann` packages
// or fully qualify the constant objects...
package com.horstmann.impatient {

  object UseConstants extends App {
    println(com.ComConstants.name)
    println(com.horstmann.HorstmannConstants.name) // println(HorstmannConstants.name) won't work
  }


}

// ## Exercise 2

// > Write a puzzler that baffles your Scala friends, using a package com that isn’t at the top level.
package puzzler.scala.com {

}


// ## Exercise 3

// > Write a package random with functions `nextInt(): Int`, `nextDouble(): Double`, and `setSeed(seed: Int): Unit`.
// To generate random numbers, use the linear congruential generator
// ```
// next = previous × a + b mod 2^n
// ```
//where a = 1664525, b = 1013904223, and n = 32.


package object random {

  val a = 1664525
  val b = 1013904223
  val n = 32

  private var _current = 1

  def nextInt(): Int = {
    _current = _current * a + b % math.pow(2, n).toInt
    _current
  }

  def nextDouble(): Double = nextInt()

  def setSeed(seed: Int): Unit = _current = seed

}

// Test our random:
package random {

  object RandomTest extends App {

    println((0 to 10).map(_ => random.nextInt()).mkString(", "))

    random.setSeed(42)
    val seeded1 = (0 to 5).map(_ => random.nextDouble())
    random.setSeed(42)
    val seeded2 = (0 to 5).map(_ => random.nextDouble())
    assert(seeded1 == seeded2)
  }

}


// ## Exercise 4

// > Why do you think the Scala language designers provided the package object syntax instead of simply letting you add functions and variables to a package?

// I am not sure. Here are my thoughts.
//
// Packages can be defined/extended in multiple files, so it could become messy and difficult to figure out what are
// the function and definitions a package includes. With object packages (only one per package level), all the definitions
// are gathered in one place.
//
// It enforce the _everything is an object_ rule.

// TODO


// ## Exercise 5
// > What is the meaning of private[com] def giveRaise(rate: Double)? Is it useful?

// `private[com]` means that the `giveRaise` method can only be called from an object inside the `com` package.
// It is not visible from the outside world (i.e. outside the base package `com`).
//
// Semantically, this means not anybody can give a raise, but only "people" part of the `com` organisation.
// But the problem is, `com` is a widely used top-level package, so in this case the restriction is not very useful.


// ## Exercise 6
// > Write a program that copies all elements from a Java hash map into a Scala hash map. Use imports to rename both classes.

object SJMap extends App {
  // Imports with aliases
  import java.util.{HashMap => JHashMap}

  import scala.collection.mutable.{HashMap => SHashMap}

  // First, let's try to do the conversion by ourselves.
  //
  // Note that there is no `fold` or equivalent defined for java maps and
  // a java map does not implements the `foreach` used in scala.
  // In the same way, an entryset is not a tuple... In short, using
  // plain old java is a pain in the *** ...
  def toScalaMap[K, V](jmap: JHashMap[K, V]): SHashMap[K, V] = {
    var smap = new SHashMap[K, V]()
    var iter = jmap.entrySet().iterator()
    while (iter.hasNext) {
      val e = iter.next
      smap(e.getKey) = e.getValue
    }

    smap
  }

  var jmap = new JHashMap[String, Int]()
  jmap.put("a", 1)
  jmap.put("b", 2)
  jmap.put("c", 3)

  println(toScalaMap(jmap)) //> Map(b -> 2, a -> 1, c -> 3)


  // Now, let's use the conversion util:
  import scala.collection.JavaConverters._
  // Wraps the java map so scala functions are available:
  mapAsScalaMap(jmap).getClass //> scala.collection.convert.Wrappers$JMapWrapper

  // Java map to an immutable scala map:
  jmap.asScala.toMap.getClass //> scala.collection.immutable.Map
  // Java HashMap to a scala HashMap:
  SHashMap[String, Int]() ++ jmap.asScala.toSeq // scala.collection.mutable.Map
}

// ## Exercise 7
// > In the preceding exercise, move all imports into the innermost scope possible.
// Here, we can simply move the import statements inside the App (which we don't have here, or inside a block

// ## Exercise 8
// > 8. What is the effect of
// ```
// import java._
// import javax._
// ```
// Is this a good idea?

// Those imports bring all `java` and `javax` subpackages into scope.
// Since there are a lot of packages (and no class) inside them, we don't really see the point. It is better to import only the packages we need.
//
// also, some packages might clash with the `scala._` ones (imported by default), such as `scala.util` (vs `java.util`): it might become misleading...


// ## Exercise 10
// > Write a program that imports the java.lang.System class, reads the user name from the user.name system property, reads a password from the Console object, and prints a message to the standard error stream if the password is not "secret". Otherwise, print a greeting to the standard output stream. Do not use any other imports, and do not use any qualified names (with dots).

object GreetingApp extends App {

  import java.lang.System._

  val CORRECT_PASS = "secret"

  // Get username:
  val name = getProperty("user.name")

  // read password using System.in:
  // (Note: using the scala console, it is way easier:
  // ```
  // val pass = Console.in.readLine
  // ```
  // but it uses _dots_ (it was `Console.readLine` before, but it has
  // been deprecated with scala 2.12)...
  var buffer = new Array[Byte](CORRECT_PASS.length)
  out.print("> ")
  in.read(buffer)
  val pass = new String(buffer)

  if (CORRECT_PASS.equals(pass)) {
    // With the scala console, `System.out` is replaced by `Console.out`:
    out.println("hello " + name)
  } else {
    // With the scala console, `System.err` is replaced by `Console.err`:
    err.println("incorrect password")
  }

}

// ## Exercise 11
// > Apart from StringBuilder, what other members of java.lang does the scala package override?

// There are plenty: `ArrayBuffer`, `HashMap`, `HashSet`, `Futures`, `math`, `List`, etc.

