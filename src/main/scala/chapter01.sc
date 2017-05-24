// # Scala For The Impatient -- Chapter 1

// ## Exercise 1
// > In the Scala REPL, type 3. followed by the Tab key. What methods can be applied?
//
// In scala, everything is an object, even an integer. `3` is of type `Int`, which defines a lot of functions and operators. See the [Int class](http://www.scala-lang.org/api/2.7.2/scala/Int.html) in the scala doc for a complete list.
3       //> res: Int = 3

// ## Exercise 2
// > In the Scala REPL, compute the square root of 3, and then square that value. By how much does the result differ from 3? (Hint: The res variables are your friend.)
//
//  The difference is of about ` -4.440892098500626E-16`.
import scala.math._
pow(sqrt(3), 2)  //> res: Double = 2.9999999999999996

// ## Exercise 3
// > Are the res variables val or var?
//
//  The results are `val`. To check it, one can do (in the scala REPL):
// ```
// scala> 3
//res0: Int = 3
//
//  scala> res0 = 2
//  <console>:12: error: reassignment to val

// ## Exercise 4
// > Scala lets you multiply a string with a number—try out "crazy" * 3 in the REPL. What does this operation do? Where can you find it in Scaladoc?
// 
// The `*` operator is redefined in the `scala.collection.immutable.StringLike` class and returns the current string concatenated n times.

"crazy" * 3    //> res: String = crazycrazycrazy
// It could also be written as a function call.
"crazy".*(3)


// ## Exercise 5
// > What does 10 max 2 mean? In which class is the max method defined?
//
// In scala, dots in function calls are optional and if a function takes only one parameter, the parentheses can be either skipped of replaced by brackets.
// `max` is defined in the `scala.runtime.RichInt` class.
10 max 2
10.max(2)

// ## Exercise 6
// > Using BigInt, compute 21024.
//
BigInt(2).pow(1024)  //> res: scala.math.BigInt = 179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137216



// ## Exercise 7
// > What do you need to import so that you can get a random prime as probablePrime(100, Random), without any qualifiers before probablePrime and Random?
//
// we need two imports.
import BigInt.probablePrime
import scala.util.Random

probablePrime(40, Random)  //> res: scala.math.BigInt = 881671071223

// ## Exercise 8
// > One way to create random file or directory names is to produce a random BigInt and convert it to base 36, yielding a string such as "qsnvbevtomcj38o06kul". Poke around Scaladoc to find a way of doing this in Scala.
//
// `scala.math.BigInt` defines a `toString` method taking a `radix` in parameter, i.e. which base we want to use.
probablePrime(100, Random).toString(36)  //> res: String = 24pw3rgurynf21kled0h

// ## Exercise 9
// > How do you get the first character of a string in Scala? The last character?
//
// A string in scala implements many of the methods used in lists and functional languages, among which `head` and `last`.
def hello = "hello"
hello.head  //> res: Char = h
hello.last  //> res: Char = o

// ## Exercise 10
// > What do the take, drop, takeRight, and dropRight string functions do? What advantage or disadvantage do they have over using substring?
//
// The main advantage is that those methods are much more concise and intuitive than java's `substring`
def s = ('a' to 'z').mkString  //>  res: String = "abcdefghijklmnopqrstuvwxyz"
// `take`: selects the first n elements
s.take(3)       //> res: String = abc
// `drop`: selects all elements except the first n elements
s.drop(3)       //> res: String = defghijklmnopqrstuvwxyz
// `takeRight`: selects the last n elements
s.takeRight(3)  //> res: String = xyz
// `dropRight`: selects all elements except the last n elements
s.dropRight(3)  //> res: String = abcdefghijklmnopqrstuvw
