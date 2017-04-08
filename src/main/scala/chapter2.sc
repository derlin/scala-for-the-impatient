
// # Scala For The Impatient -- Chapter 2

// ## Exercise 1
// > The signum of a number is 1 if the number is positive, –1 if it is negative, and 0 if it is zero. Write a function that computes this value.

// using _if/else_:
def signum1(x: Int) = if (x == 0) 0 else if (x < 0) -1 else 1

// using _pattern matching_:
def signum2(x: Int) = x match {
  case 0 => 0
  case _ if (x > 0) => 1
  case _ => -1
}

// ## Exercise 2
// > What is the value of an empty block expression {}? What is its type?
//
// An empty block as type `Unit`.  As explained in the [API docs](http://www.scala-lang.org/api/current/index.html#scala.Unit):
//
// > Unit is a subtype of scala.AnyVal. There is only one value of type Unit, (), and it is not represented by any object in the underlying runtime system. A method with return type Unit is analogous to a Java method which is declared void.
//
// So, `Unit` is a sort of placeholder meaning _no useful value_.


// ## Exercise 3
// > Come up with one situation where the assignment x = y = 1 is valid in Scala.
//
// Since in scala an assignment does return nothing (vs C-like languages), `x` needs to be of type Unit
var x = {} //> res: x = Unit ()
var y = 0 //> res: y = 0
x = y = 1

// ## Exercise 4
// > Write a Scala equivalent for the Java loop
// ```
//  for (int i = 10; i >= 0; i--) System.out.println(i);
// ```

// Using `reverse` (not very elegant):
for (i <- 0 to 10 reverse) println(i)

// Using `by` to control the increment:
for (i <- 10 to 0 by -1) println(i)

// ## Exercise 5
// > Write a procedure countdown(n: Int) that prints the numbers from n to 0.

/* Using a for loop */
def countdown1(x: Int) =
  for (i <- x to 0 by -1) println(i)

/* Using recursion */
def countdown2(x: Int): Unit = if (x > 0) {
  println(x)
  countdown2(x - 1)
}


// ## Exercise 6
// > Write a for loop for computing the product of the Unicode codes of all letters in a string. For example, the product of the characters in "Hello" is 825152896.
def unicodeProduct(s: String) = {
  var i = 1
  for (c <- s) i *= c.toInt
  i
}

// ## Exercise 7, 8 and 9
// > 7. Solve the preceding exercise without writing a loop. (Hint: Look at the StringOps Scaladoc.)
//
// > 8. Write a function product(s : String) that computes the product, as described in the preceding exercises.
//
// > 9. Make the function of the preceding exercise a recursive function.
//
// _Note:_ in the functions below, we should take care of overflows. In the following, we use `Int` (that's just an exercise), but in production we should use `BigInt`...

def s = "Hello"

// Using `foreach`:
def product1(s: String) = {
  var i = 1
  s.foreach(i *= _)
  i
}
product1(s)

// Using recursion (don't forget to specify the return type):
def product2(s: String): Int = {
  if (s.length == 0) 1
  else s.head * product2(s.tail)
}
product2(s)

// Using tail recursion (more effective):
//
// <div style="border:1px solid #eee; padding:5px 5px 5px 15px; font-size: .9em">__Tail recursion__ is a special kind of recursion where the recursive call is the very last thing in the function. It's a function that _does not do anything at all after recursing_. <br/>
// This is important because it means that you can just pass the result of the recursive call through directly instead of waiting for it, so you don't have to consume any stack space. </div>
//
// the `@tailrec` annotation checks that the function is indeed a tail recursion.
//
// We could also have used one function with a default `acc` parameter set to 1, but using a private inner function ensures no one tempers with the `acc` (for example calling product(s, 0)...)
def product3(s: String) = {
  @scala.annotation.tailrec
  def _product(s: String, acc: Int): Int = {
    if (s.length == 0) acc
    else _product(s.tail, acc * s.head)
  }

  _product(s, 1)
}
product3(s)

// Using `foldLeft`.
// This is certainly the most elegant, but also the most difficult to
// understand...
def product4(s: String) = s.foldLeft(1)(_ * _)
product4(s)


// ## Exercise 9
// > Write a function that computes x , where n is an integer. Use the following recursive definition (omitted).

// Using if/else:
def power1(x: Double, n: Int): Double =
  if (n == 0) 1
  else if (n < 0) 1 / power1(x, -n)
  else if (n % 2 == 0 && n > 2) power1(power1(x, n / 2), 2)
  else x * power1(x, n - 1)

power1(2, 10)  //> 1024.0
power1(2, 1)   //> 2.0
power1(2, 13)  //> 8192.0
power1(2, -4)  // 0.0625

// Using _pattern matching_:
def power2(x: Double, n: Int): Double = n match {
  case 0 => 1
  case n if (n < 0) => 1 / power2(x, -n)
  case n if (n % 2 == 0 && n > 2) => power2(power2(x, n / 2), 2)
  case _ => x.toDouble * power2(x, n - 1)
}

power2(2, 10)  //> 1024.0
power2(2, 1)   //> 2.0
power2(2, 13)  //> 8192.0
power2(2, -4)  //> 0.0625
