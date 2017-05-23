// # Scala For The Impatient -- Chapter 12

// ## Exercise 1
// > Write a function that, given a string, produces a map of the indexes of all characters. For example, indexes("Mississippi")` should return a map associating 'M' with the set {0}, 'i' with the set {1, 4, 7, 10}, and so on. Use a mutable map of characters to mutable sets. How can you ensure that the set is sorted?

def indexes(s: String) = {
  import scala.collection.mutable
  var map = mutable.SortedMap[Char, mutable.Set[Int]]()

  for (c <- s.zipWithIndex)
    map.getOrElseUpdate(c._1, mutable.SortedSet[Int]()) += c._2
  map
}

// yields:
//```
// M -> TreeSet(0)
// i -> TreeSet(1, 4, 7, 10)
// p -> TreeSet(8, 9)
// s -> TreeSet(2, 3, 5, 6)
//```
indexes("Mississippi").mkString("\n")

// ## Exercise 2
// > Repeat the preceding exercise, using an immutable map of characters to lists

def indexes2(s: String) = {
  s.zipWithIndex.foldLeft {Map.empty[Char, Set[Int]]} {
    case (acc, c) => acc + ((c._1, acc.get(c._1) match {
      case Some(x) => x + c._2
      case _ => Set(c._2)
    }))
  }
}

indexes2("Mississippi").mkString("\n")



// ## Exercise 3
// >  Write a function that removes all zeroes from a linked list of integers.
def noZeroes(ls: scala.collection.mutable.LinkedList[Int]) = ???


// ## Exercise 4
// > Write a function that receives a collection of strings and a map from strings to integers. Return a collection of integers that are values of the map corresponding to one of the strings in the collection. For example, given Array("Tom", "Fred", "Harry") and Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5),  return Array(3, 5)


// `flatMap` => get rid of `Option` and keeps only the `Some`
def exo4_v1(ss: Seq[String], map: Map[String, Int]) =
  ss.flatMap(map.get).toList

exo4_v1(Array("Tom", "Fred", "Harry"), Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5))

def exo4_v2(ss: Seq[String], map: Map[String, Int]) =
  ss.map(map.get).collect { case Some(x) => x }.toList

exo4_v2(Array("Tom", "Fred", "Harry"), Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5))

def exo4_v3(ss: Seq[String], map: Map[String, Int]) =
  ss.map(map.get).flatten.toList

exo4_v3(Array("Tom", "Fred", "Harry"), Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5))



// ## Exercise 5
// > Implement a function that works just like mkString, using reduceLeft.

def myMkString[A](ls: Seq[A], sep: String = ""): String =
  ls.
    map(_.toString). // mandatory: reduce -> type in == type out
    reduceLeft { (acc, in) => acc + sep + in }

val fruits = List("apple", "banana", "ananas")
myMkString(fruits) //> applebananaananas
myMkString(fruits, ", ") //> apple, banana, ananas

// ## Exercise 6
//> Given a list of integers lst, what is
// ```
// (lst :\ List[Int]())(_ :: _)
// (List[Int]() /: lst)(_ :+ _)
// ```
// ? How can you modify one of them to reverse the list?

val lst = (1 to 10).toList

// from the right, we add elements to a new list. So basically,
// it duplicates the elements of the list.
(lst :\ List[Int]()) (_ :: _) // ==>

lst.foldRight(List.empty[Int]) {
  (i, acc) => acc.+:(i) // or i +: acc or i :: acc. :: or +: = append to a list
}

// using foldLeft,  we begin at the first element and add new elements to the tail of the list, so we also duplicate it.
(List[Int]() /: lst) (_ :+ _) // ==>

lst.foldLeft(List.empty[Int]) {
  (acc, i) => acc ++ List(i) // :+ = prepend, i.e. add to the beginning, same as ++ List()
}


// ## Exercise 7

(1 to 4).zip(4 to 8) map {Function.tupled(_ * _)} //> Vector(4, 10, 18, 28)

// ## Exercise 8
//> Write a function that turns an array of Double values into a two-dimensional array. Pass the number of columns as a parameter. For example, with Array(1, 2, 3, 4, 5, 6) and three columns, return Array(Array(1, 2, 3), Array(4, 5, 6)). Use the grouped method.


def toMultiDim[T](arr: List[T], col: Int): List[List[T]] = arr.grouped(col).toList

def toMultiDim_v2[T](lst: List[T], col: Int): List[List[T]] = {
  for (i <- lst.indices by col) yield lst.slice(i, i + col)
}.toList

def toMultiDim_v3[T](lst: List[T], col: Int): List[List[T]] = {
  lst.foldRight(List(List.empty[T])) {
    (i, acc) =>
      acc.head match {
        case l@List(_*) if l.length < 3 => (i :: l) :: acc.tail
        case _ => List(i) :: acc
      }
  }
}

val arr = List(1, 2, 3, 4, 5, 6)
toMultiDim(arr, 3)
toMultiDim_v2(arr, 3)
toMultiDim_v3(arr, 3)

// ## Exercises  9 and 10
// TODO