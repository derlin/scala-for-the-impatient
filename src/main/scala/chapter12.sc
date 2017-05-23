// # Scala For The Impatient -- Chapter 11

// ## Exercise 1
// >  Write a function `values(fun: (Int) => Int, low: Int, high: Int)` that yields a collection of function inputs and outputs in a given range. For example, `values(x => x * x, -5, 5)` should produce a collection of pairs `(-5, 25), (-4, 16), (-3, 9), . . . , (5, 25)`.

// here, if we use the notation `(_, fun(_))`, `fun(_)` yields a lambda vs calls fun
def values(fun: (Int) => Int, low: Int, high: Int) =
  (low to high).map { i => (i, fun(i)) }

values(x => x * x, -5, 5)

// ## Exercise 2
// > How do you get the largest element of an array with reduceLeft?

// with generics:
// _note_: `<%` means "_can implicitly be converted to the defined type_". Since `Int => Ordered` is implicit and not a direct extension, we cannot use `<:`...

def largestElt[T <% Ordered[T]](a: Array[T]) =
  a.reduceLeft((a, b) => if (a > b) a else b)

largestElt(Array(1, 2, 3, 4, -1))

// to work only with Int:
Array(1, 2, 3, 4, -1).reduceLeft(_ max _)

// ## Exercise 3
// > Implement the factorial function using to and reduceLeft, without a loop or recursion.

def facto(n: Int) = n match {
  case _ if n < 1 => 0
  case _ => (1 to n).reduceLeft(_ * _)
}
facto(0)
facto(1)
facto(10)


// ## Exercise 4
// >  The previous implementation needed a special case when n < 1. Show how you can avoid this with foldLeft.

// we use n to initialize the accumulator. For this to work, we need to loop from 1 to n-1, not from 1 to n ! Hence the use of `until` instead of `to`
def facto2(n: Int) = (1 until n).foldLeft(n)(_ * _)

facto2(0)
facto2(1)
facto2(10)

// ## Exercise 5
// > Write a function `largest(fun: (Int) => Int, inputs: Seq[Int])` that yields the largest value of a function within a given sequence of inputs. For example, `largest(x => 10 * x - x * x, 1 to 10)` should return 25. Don’t use a loop or recursion.

// very smooth and efficient
def largest(fun: (Int) => Int, inputs: Seq[Int]) =
  inputs.map(fun).max

largest(x => 10 * x - x * x, 1 to 10)

// with fold: here, fun(x) is computed potentially multiple times for each input...
def largestFold(fun: (Int) => Int, inputs: Seq[Int]) =
  inputs.foldLeft(Int.MinValue) { (acc, in) => if (fun(in) > acc) fun(in) else acc }

largestFold(x => 10 * x - x * x, 1 to 10)

// ## Exercise 6
// >  Modify the previous function to return the input at which the output is largest.

def largestInput(fun: (Int) => Int, inputs: Seq[Int]) =
  inputs.map { i => (fun(i), i) }.max._2

largestInput(x => 10 * x - x * x, 1 to 10)

// here, we can use reduce, since both acc and inputs are the same
def largestInputFold(fun: (Int) => Int, inputs: Seq[Int]) =
  inputs.reduce { (a, b) => if (fun(a) > fun(b)) a else b }

largestInputFold(x => 10 * x - x * x, 1 to 10)

// ## Exercise 7
//> Write a function `adjustToPair` that receives a function of type (Int, Int) => Int and returns the equivalent function that operates on a pair. For example, `adjustToPair(_ * _)((6, 7))` is 42.

val pairs = (1 to 10) zip (11 to 20)

def adjustToPair[A, B, C](fun: (A, B) => C)(pair: (A, B)): C =
  pair match {case (a, b) => fun(a, b)}

// since we use generics, we need to tell the compiler what the types are in the partial function...
adjustToPair((_: Int) * (_: Int))((6, 7)) //> 42
pairs.map(adjustToPair(_ + _)) //> (12, 14, 16, 18, 20, 22, 24, 26, 28, 30)

// ## Exercise  8
// >  Make a call to corresponds (see page 149) that checks whether the elements in an array of strings have the lengths given in an array of integers.

val arrayOfString = Array("Hello", "world", "!", ":)")
val lengthOfStrings = arrayOfString.map(_.length)

arrayOfString.corresponds(lengthOfStrings)(_.length == _)


// ## Exercise 9
// Implement corresponds without currying. Then try the call from the preceding exercise. What problem do you encounter?

// Two main problems:

// 1. since the function has 3 parameters, we cannot use the infix notation, i.e. x corresponds y
// 2. type inference is not working properly, we have to specify the types in the lambda function
//
// With currying, both those problems are solved.

def myCorresponds[A, B](in1: Seq[A], in2: Seq[B], fun: (A, B) => Boolean) = in1.zip(in2).forall(t => fun(t._1, t._2))

myCorresponds(arrayOfString, lengthOfStrings, (a: String, b: Int) => a.length == b)


// ## Exercise 10
// >  Implement an unless control abstraction that works just like if, but with an inverted condition. Does the first parameter need to be a call-by-name parameter? Do you need currying?

// Currying let's you write something that is more like the `if` statement. But it is not needed per se.

// recall that `: => ` means a call-by-name
def unless(condition: => Boolean)(body: => Unit) =
  if (!condition) body

unless(0 == 1) {
  println("it works !")
}

// notice that we _have to_ use the curly brackets. It is still a function call, so
// `unless(x) println()` won't work.
for (i <- (0 to 5)) unless(i == 3) {println(i)}
