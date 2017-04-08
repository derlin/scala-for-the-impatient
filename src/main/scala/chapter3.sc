// # Scala For The Impatient -- Chapter 3

// ## Exercise 1
// > Write a code snippet that sets a to an array of n random integers between 0 (inclusive) and n (exclusive).
//
def randomArray(length: Int, min: Int, max: Int) = {
  // Importing `Random` is not necessary, but shows how we can import something only in a block.
  import scala.util.{Random => R}
  // In case `min > max`, we cannot return an Int... So we use the useful `sys.error` to throw a `RuntimeException` with a meaningful message.
  if (min >= max) sys.error("min should be < max")
  // `yield` will gather all the results `yielded` inside the loop into a collection. Very useful !
  // Also, we can wrap the for statement into parentheses so that we can convert the result (a `Vector` by default) to an array
  (for (_ <- 0 until length)
    yield min + (R.nextDouble() * (max - min)).toInt
    ).toArray
}

randomArray(10, 10, 20) //> Array(11, 15, 14, 19, 15, 14, 19, 14, 16, 17)


// ## Exercise 2
// > Write a loop that swaps adjacent elements of an array of integers. For example, Array(1, 2, 3, 4, 5) becomes Array(2, 1, 4, 3, 5).
//
// Here, ensure you use `Array` and not `List`, as a list in scala is immutable.
def swap[T](array: Array[T]) = {
  for (i <- 0 until array.length - 1 by 2) {
    val temp = array(i)
    array(i) = array(i + 1)
    array(i + 1) = temp
  }
  array
}

// The swap is done in-place
var l1 = (0 to 10).toArray
swap(l1) //> Array(1, 0, 3, 2, 5, 4, 7, 6, 9, 8, 10)
// but since the last statement of the `swap` method is `array`, the array is also returned, making the following work as well:
l1 = swap((0 to 10).toArray) //> Array(1, 0, 3, 2, 5, 4, 7, 6, 9, 8, 10)

// ## Exercise 3
// > Repeat the preceding assignment, but produce a new array with the swapped values. Use for/yield.
//
// There are many possibilities. Here, I have two for loops, the second one permits me to traverse elements i-1 and i. Also, since we create a new object, an immutable list seems more appropriate.
def swapList[T](list: List[T]) = {
  (for (i <- 1 until list.length + 1 by 2;
        j <- (i - 1 to i reverse) if j < list.length)
    yield j
    ).toList
}

swapList(0 to 10 toList) //> List(1, 0, 3, 2, 5, 4, 7, 6, 9, 8, 10)

// ## Exercise 4
// > Given an array of integers, produce a new array that contains all positive values of the original array, in their original order, followed by all values that are zero or negative, in their original order.

// Using _filter_:
//
// (the `++` operator concatenates lists)
def positiveFirst1(list: List[Int]) = {
  val pos = list.filter((_: Int) > 0)
  val neg = list.filterNot((_: Int) > 0)
  pos ++ neg
}

// Using _for_ and _yield_:
def positiveFirst2(list: List[Int]) =
  (for (elt <- list if elt > 0) yield elt) ++ (for(elt <- list if elt <= 0) yield elt)

// Testing the two codes:
val randomL = List(3, -5, 7, -4, 2, -10, -9, 0, 9, -10)
positiveFirst1(randomL) //> List(3, 7, 2, 9, -5, -4, -10, -9, 0, -10)
positiveFirst2(randomL) //> List(3, 7, 2, 9, -5, -4, -10, -9, 0, -10)

// ## Exercise 5
// > How do you compute the average of an Array[Double]?
def avg(list: List[Double]) = {
  list.sum / list.length
}

// Testing:
avg({
  for (i <- 0 to 10) yield scala.util.Random.nextDouble()
}.toList)


// ## Exercise 6
// > How do you rearrange the elements of an Array[Int] so that they appear in
// reverse sorted order?

// 1. sort, then reverse:
def reverseSort1(array : Array[Int]) = array.sorted.reverse
// 2. use `sortWith`, the lambda should return true if a comes before b, false otherwise:
def reverseSort2(array : Array[Int]) = array.sortWith(_ > _)
// 2. use `sortBy`, using the unary `-` to reverse the order:
def reverseSort3(array : Array[Int]) = array.sortBy(-_)

var array = (0 to 10).toArray
reverseSort1(array)
reverseSort2(array)
reverseSort3(array)

// > How do you do the same with an ArrayBuffer[Int]?
// To use an `ArrayBuffer`, we only need to change the method's signature.
//
// This is because the sort methods are implemented in `scala.collection.SeqLike`, which both `Array` and `ArrayBuffer` implement.
import scala.collection.mutable.ArrayBuffer
(0 to 10).toBuffer.asInstanceOf[ArrayBuffer[Int]].sortBy(-_)


// ## Exercise 7
// > Write a code snippet that produces all values from an array with duplicates removed. (Hint: Look at Scaladoc.)
// Use the `distinct` method of `scala.collection.SeqLike`.
var dupl = (0 to 10) ++ (1 to 11)
dupl.distinct

// ## Exercise 8
// > Rewrite the example at the end of Section 3.4, “Transforming Arrays,” on page 34 using the drop method for dropping the index of the first match. Look the method up in Scaladoc.
// `drop` drops the n first elements of a sequence.
def dropNegButFirst(array: Array[Int]) = {
  var idx = for (i <- array.indices if array(i) < 0) yield i
  if (idx.length == 0)
    array
  else {
    idx = idx.drop(1)
    var buff = array.toBuffer
    for (i <- idx.reverse) buff.remove(i)
    buff.toArray
  }
}

dropNegButFirst((1 :: 2 :: (-3) :: (-2) :: 0 :: Nil).toArray)

// ## Exercise 9
// > Make a collection of all time zones returned by java.util.TimeZone.getAvailableIDs that are in America. Strip off the "America/" prefix and sort the result.

def americanTz = {
  for (tz <- java.util.TimeZone.getAvailableIDs
       if tz.startsWith("America"))
    yield tz.replace("America/", "")
}
americanTz

// ## Exercise 10
// > Import java.awt.datatransfer._ and make an object of type SystemFlavorMap with the call
//
//    val flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]
//
//Then call the getNativesForFlavor method with parameter DataFlavor.imageFlavor and get the return value as a Scala buffer. (Why this obscure class? It’s hard to find uses of java.util.List in the standard Java library.)

// The `getNativesForFlavor` returns a `java.util.List`
import java.awt.datatransfer._

val flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]

var javaList = flavors.getNativesForFlavor(DataFlavor.imageFlavor) // java.util.List[String] = [PNG, JFIF, TIFF]

// We can use the `toArray` and `toBuffer` methods, which seem to work:
javaList.toArray.toBuffer // scala.collection.mutable.Buffer[Object] = ArrayBuffer(PNG, JFIF, TIFF)

// We can also use the (now _deprecated_) `JavaConversions` util:
collection.JavaConversions.asScalaBuffer(javaList) // scala.collection.mutable.Buffer[String] = Buffer(PNG, JFIF, TIFF)

// The scala 2.12 way would be:
scala.collection.JavaConverters.asScalaBuffer(javaList) // scala.collection.mutable.Buffer[String] = Buffer(PNG, JFIF, TIFF)



