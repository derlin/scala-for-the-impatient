// # Tips and Tricks

// ## flatMap

// flatMap treats a string as a sequence of Char
val fruits = Seq("apple", "banana", "orange")
fruits.map(_.toUpperCase) //> List(APPLE, BANANA, ORANGE)
fruits.flatMap(_.toUpperCase) //> List(A, P, P, L, E, B, A, N, A, N, A, O, R, A, N, G, E)
fruits.map(_.toUpperCase).flatten //> same as flatMap

// flatMap is equivalent to `map.flatten`, so we can do things like:
def g(n: Int) = List(n, n, n)

(1 to 3).toList.map(g) //> List(List(1, 1, 1), List(2, 2, 2), List(3, 3, 3))
(1 to 3).toList.flatMap(g) //> List(1, 1, 1, 2, 2, 2, 3, 3, 3)

// but flatMap is also very useful for function that do not always return something, i.e. returns an `Option`. Example:

def toInt(s: String) =
  try {Some(s.toInt)} catch {case e: Exception => None}

val numStrings = List("1", "2", "foo", "3")
numStrings.map(toInt) //> List(Some(1), Some(2), None, Some(3))
numStrings.flatMap(toInt) //> List(1, 2, 3)

// It is also very useful for Maps, since `map.get` returns an option:
val map = Map(1 -> 'a', 2 -> 'b', 3 -> 'c')

val keys = List(1, 3, 5)
keys.map(map.get) //> List(Some(a), Some(c), None)
keys.flatMap(map.get) //> List(a, c)


// Why does `flatMap` works with `Option` ? This is because `Option` defines many of the functions on list. More importantly, `Option` is traversable, so it works with the `flatMap` definition:
// ```
// final override def flatMap[B, That](f: (A) => GenTraversableOnce[B])
// (implicit bf: CanBuildFrom[List[A], B, That]): That
// ```

Some(2).foreach(println) //> 2


// ## Collect
// Collect is very useful since it allows for partial function, i.e. the use of `case` without all matching case. Only matched cases are kept in the result:

List("a", 1, 4.55, "b", 10).collect {
  case x: String => x
  case i: Int if (i > 2) => i
} //> List(a, b, 10)


// # List append vs array append
// Careful on the representation !!

// When working with an array, it is _appending_ that is efficient
var arr = Array(1, 2).toBuffer
arr.append(3)
arr.toArray //> Array(1, 2, 3)

// Using lists, it is _prepending_ that is efficient
// (recall: Append (`:+`) or prepend (`+:`) to a sequence.
val lst = List(1, 2)
// "prepend 3 to lst" => List(3, 1, 2)
3 +: lst
3 :: lst

// "append 3 to lst" => List(1, 2, 3)
lst :+ 3
lst ++ List(3)

// # Working with zip and tuples

// When zipping, you get a tuple so it is not possible to write something like:
// ```
// prices.zip(quantities).map{ _ * _ }
// ```
// To overcome that, use `Function.tupled`:

List(1, 2).zip(List(3, 4)).map {Function.tupled(_ * _)} //> List(3, 8)

// # Useful list/array methods
val someList = (1 to 10).toList
// use `indices` vs `0 until list.length`
someList.indices //> Range 0 until 10
// group elements with `grouped`
someList.grouped(2).toList //> List( List(1,2), List(3,4) ...  )
// get a slice of an array using `slice(from_inclusive, to_exclusive)`
someList.slice(2, 5)

//> List(3, 4, 5)

// # Generics
// There is type erasure, _except for List_ !

// # Varargs

// to specify varargs, use `Class*`. For example:
case class BagOfWords(descr: String, bag: String*)

val bag1 = BagOfWords("empty bag")
val bag2 = BagOfWords("bag of two", "hello", "world")

// then, to use it in pattern matching, use `*_` with an alias:
def bagsMatch(bag: BagOfWords) = bag match {
  case BagOfWords(descr) => "EMPTY: " + descr
  case BagOfWords(descr, alias@_*) => "NOT EMPTY: " + alias.mkString(", ")
}

bagsMatch(bag1) //> EMPTY: empty bag
bagsMatch(bag2) //> NOT EMPTY: hello, world

// # Pattern matching

// Notice how we can specify types and use List(_*):
def leafSum(tree: List[Any]): Int = tree match {
  case (head: Int) :: tail => head + leafSum(tail)
  case (head@List(_*)) :: tail => leafSum(head) + leafSum(tail)
  case Nil => 0
}

val tree = List(List(3, 8), 2, List(5))
leafSum(tree) //> 18


