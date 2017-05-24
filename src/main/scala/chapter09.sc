// # Scala For The Impatient -- Chapter 9

// we'll need it to write to files
import java.io.PrintWriter

// ## Exercise 1
// > Write a Scala code snippet that reverses the lines in a file (making the last line the first one, and so on)


// quote:
// > You have gotta dance like there is nobody watching
// > Love like you will never be hurt
// > Sing like there's nobody listening
// > And live like it is heaven on earth
//
// result: (I let you guess ^^)
val filename = "/tmp/quote.txt"
io.Source.fromFile(filename)
  .getLines.toArray
  .reverse
  .mkString("\n")


// ## Exercise 2
// > Write a Scala program that reads a file with tabs, replaces each tab with spaces so that tab stops are at n-column boundaries, and writes the result to the same file.

def tabs2spaces(filename: String, tabSize: Int = 4): Unit = {
  import java.io.PrintWriter

  val textWithSpaces = io.Source.fromFile(filename).mkString
    .replaceAll("\t", " " * tabSize)

  new PrintWriter(filename) {write(textWithSpaces); close}

}

//tabs2spaces("/tmp/fileWithTabs.txt")

// ## Exercise 3
// > Write a Scala code snippet that reads a file and prints all words with more than 12 characters to the console. Extra credit if you can do this in a single line.

// Note: here, I print 6 chars or more, since my quote does not have
// many long words. Result: watching, listening.
io.Source.fromFile(filename).mkString
  .split("\\W+")
  .filter(_.length > 6)
  .foreach(println(_))

// ## Exercise 4
// > Write a Scala program that reads a text file containing only floating-point numbers. Print the sum, average, maximum, and minimum of the numbers in the file.

def statsFromFile(filename: String): Unit = {
  val (count, sum, min, max) =
    io.Source.fromFile(filename).mkString
      .split("[^0-9\\.-]+")
      .map(_.toFloat)
      .foldLeft((0f, 0f, Float.MaxValue, -Float.MaxValue)) {
        case ((count, sum, min, max), elt) => (
          count + 1,
          sum + elt,
          if (elt < min) elt else min,
          if (elt > max) elt else max
        )
      }
  println(s"sum=$sum, avg=${sum / count}, min=$min, max=$max")
}

statsFromFile("/tmp/floats.txt")

// ## Exercise 5
// > Write a Scala program that writes the powers of 2 and their reciprocals to a file, with the exponent ranging from 0 to 20. Line up the columns.

// output:
// ```
//   exp |      power | reciprocal
//   =============================
//     0 |          1 |   1.000000
//     1 |          2 |   0.500000
//     2 |          4 |   0.250000
//     3 |          8 |   0.125000
//     4 |         16 |   0.062500
//     5 |         32 |   0.031250
//     6 |         64 |   0.015625
//     7 |        128 |   0.007813
//     8 |        256 |   0.003906
//     9 |        512 |   0.001953
//    10 |       1024 |   0.000977
//    11 |       2048 |   0.000488
//    12 |       4096 |   0.000244
//    13 |       8192 |   0.000122
//    14 |      16384 |   0.000061
//    15 |      32768 |   0.000031
//    16 |      65536 |   0.000015
//    17 |     131072 |   0.000008
//    18 |     262144 |   0.000004
//    19 |     524288 |   0.000002
//    20 |    1048576 |   0.000001
// ```

def powersOf2(filename: String): Unit = {
  val printer = new PrintWriter(filename)
  printer.write("exp | %10s | %10s%n".format("power", "reciprocal"))
  printer.write("=============================\n")

  // the fold can be replaced by a basic loop:
  //```
  // var exp = 1
  // var rec = 1.0
  // for (i <- 0 to 20) {
  //   printer.write("%3d | %10d | %10f%n".format(i, exp, rec))
  //   exp *= 2
  //   rec /= 2
  // }
  //```
  (0 to 20).foldLeft((1, 1.0)) {
    case ((exp, rec), i) => {
      printer.write("%3d | %10d | %10f%n".format(i, exp, rec))
      (exp * 2, rec / 2)
    }
  }

  printer.close
}

powersOf2("/tmp/powersOf2.txt")


// ## Exercise 6
// > Make a regular expression searching for quoted strings "like this, maybe with \" or \\" in a Java or C++ program. Write a Scala program that prints out all such strings in a source file.

// We want to find _an opening quote, anything being either an escaped quote or something else than a quote, then a closing quote_. In regex language:
//  * with special """ escape:
//      ```
//      """"((?:(?:\\")|[^"])*)"""".r
//      ```
//  * the same without special escape:
//      ```
//      "\"((?:(?:\\\\\")|[^\"])*)\"".r
//      ```
//  * using non-greedy match: `
//      ```
//      """"((?:\\"|.)*?)"""".r
//      ```
// The `?:` after an opening parenthesis signify the parentheses don't denote a capturing group.
def printStrings(text: String) = {

  val regex = """"((?:(?:\\")|[^"])*)"""".r
  // use `for(m <- regex.findAllIn(text)` to print the string with the quotes
  for (regex(m) <- regex.findAllIn(text))
    println(s" -> $m")

}

// To read from a file, use:
// ```
// printStrings{
//  io.Source.fromFile(filename).mkString
// }
// ```
printStrings {
  """
   "this"
   another line
   and "this \" and \" that \\"
  """.stripMargin
} //>  -> "this"
//> -> "this \" and \" that \\"

// ## Exercise  7
// > Write a Scala program that reads a text file and prints all tokens in the file that are not floating-point numbers. Use a regular expression.

// Here is a simple tokenizer skipping floats. Another way would be:
// ```
// floatingPointRegex.replaceAllIn(content, "").split("\\s")
// ```
{
  val floatingPointRegex = "[0-9]\\.[0-9]+".r

  def tokeniseSkipFloats(content: String) =
    content.split("\\s+").foreach {
      w => if (!floatingPointRegex.findFirstIn(w).isDefined) println(w)
    }

  println(tokeniseSkipFloats {
    io.Source.fromFile("/tmp/text-with-floats.txt").mkString
  })
}

// ## Exercise 8
// > Write a Scala program that prints the src attributes of all img tags of a web page. Use regular expressions and groups.

// For a quick try, use a simple string:
val text =
  """hello <img src="https://mdn.mozillademos.org/files/12708/image-with-title.png"
     alt="The dinosaur image"
     style="display: block; height: 341px; margin: 0px auto; width: 400px;">"""
    .replace("\n", "")

// One can also directly download an real HTML page
var html = io.Source.fromURL("https://developer.mozilla.org/en-US/docs/Learn/HTML/Multimedia_and_embedding/Images_in_HTML").mkString

// We can use pattern matching in the for loop using `regexName(group1, group2, ...) <- `:
val imgSourceRegex = "<img.*src=(?:\"|')([^\"']+).*>".r
for (imgSourceRegex(src) <- imgSourceRegex.findAllIn(html))
  println(src)

// If one don't want to use pattern matching, here is another [long] syntax.
// Note that in this case, _group 0_ denotes the whole match.
for (m <- imgSourceRegex.findAllIn(html).matchData)
  println(m.group(1))


// ## Exercise 9
// > Write a Scala program that counts how many files with .class extension are in a given directory and its subdirectories.

{
  // There are currently no official Scala classes for visiting all files in a directory, so we will use the java ones...
  import java.io.File

  // Define a function for iterating on all files under a certains path, recursively. By returning an `Iterator`, it is possible to use the notation: `for(f <- walkFiles(dir))`:
  def walkFiles(rootDir: File): Iterator[File] = {
    val children = rootDir.listFiles
    children.filter(_.isFile).toIterator ++
      children.filter(_.isDirectory).flatMap(walkFiles _)
  }

  // Use the `walkFiles` method to count `.class` files
  def countClassFiles(rootDir: File) =
    walkFiles(rootDir)
      .filter(_.getName.endsWith(".class"))
      .length

  // Do some testing:
  val path = "/Users/Lin/git/scala" // replace by your own path
  countClassFiles(new File(path)) // my answer is a poor 14

}

// ## Exercise 10
// > Expand the example with the serializable `Person` class that stores a collection of friends. Construct a few `Person` objects, make some of them friends of another, and then save an `Array[Person]` to a file. Read the array back in and verify that the friend relations are intact.

// __Important !!__: for the following to work, ensure you use a scala class (and not the scala worksheet). Indeed, the worksheet does not generate class files, so `Person.class` will be unknown during deserialisation... 

import scala.collection._

@SerialVersionUID(42L) class Person(val name: String, friends: Person*) extends Serializable {

  private[Person] var _friends = new mutable.HashSet[Person]()

  addFriends(friends: _*)

  // Friendship is idempotent:
  // ```
  // a [friends with] b <=> b [friends with] a
  // ```
  def addFriends(friends: Person*): Array[Person] = {
    for (f <- friends)
      if (!_friends.contains(f)) {
        f._friends.add(this)
        _friends.add(f)
      }

    getFriends
  }

  def getFriends() = _friends.toArray

  override def toString =
    s"Person[$name, friends=${_friends.map(_.name).mkString(",")}]"
}

object MyApp extends App {
  val p1 = new Person("Jacques")
  val p2 = new Person("Damien")
  val p3 = new Person("Lucy", p1, p2)

  // This should yield:
  // ```
  // Person[Jacques, friends=Lucy]
  // Person[Damien, friends=Lucy]
  // Person[Lucy, friends=Jacques,Damien]
  // ```
  println(p1)
  println(p2)
  println(p3)

  // Serialize to temp file:
  import java.io._

  val tempFile = "/tmp/test.obj"
  val out = new ObjectOutputStream(new FileOutputStream(tempFile))
  out.writeObject(List(p1, p2, p3))
  out.close()
  // Read back the list and print the objects:
  val in = new ObjectInputStream(new FileInputStream(tempFile))
  val deser = in.readObject().asInstanceOf[List[Person]]
  for (p <- deser) println(p)
}
