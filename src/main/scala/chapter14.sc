// # Scala For The Impatient -- Chapter 13

// # Exercise 2
//> Using pattern matching, write a function swap that receives a pair of integers and returns the pair with the components swapped.

def swap(t: (Int, Int)): (Int, Int) = t match {case (x, y) => (y, x)}
def swap_(t: (Int, Int)): (Int, Int) = (t._2, t._1)

swap((1, 2))
swap_((1, 2))

// # Exercise 2
// > Using pattern matching, write a function swap that swaps the first two elements of an array provided its length is at least two.

def swapFirstTwo[T](lst: List[T]) = lst match {
  case List(x, y, tail@_*) => y +: x +: tail
  case other => other
}

swapFirstTwo((1 to 5).toList) //> List(2, 1, 3, 4, 5)
swapFirstTwo(List(1, 2)) //> List(2, 1)
swapFirstTwo(List(1)) //> List(1)
swapFirstTwo(List())

//> List()

// # Exercise 3
//>  Add a case class `Multiple` that is a subclass of the `Item` class. For example, Multiple(10, Product("Blackwell Toaster", 29.95)) describes ten toasters. Of course, you should be able to handle any items, such as bundles or multiples, in the second argument. Extend the price function to handle this new case.

abstract class Item {
  def thePrice(): Double = this match {
    case Article(_, price) => price
    case Bundle(_, discount, items@_*) => items.map(_.thePrice).sum - discount
    case Multiple(n, item) => n * item.thePrice
  }
}

case class Article(description: String, price: Double) extends Item

case class Bundle(description: String, discount: Double, items: Item*) extends Item

case class Multiple(n: Int, item: Item) extends Item

val bundle = Bundle("Father's day special", 20.0,
  Article("Scala for the Impatient", 39.95),
  Bundle("Anchor Distillery Sampler", 10.0,
    Article("Old Potrero Straight Rye Whiskey", 79.95),
    Article("Junípero Gin", 32.95)))

val multiple = Bundle("another bundle", 10.0, Multiple(4, Article("Rubber band", 20)))

bundle.thePrice //> 122.85000000000002
multiple.thePrice //> 70.0


// # Exercise  5
//>  One can use lists to model trees that store values only in the leaves. For example, the list ((3 8) 2 (5)) describes a tree.  Write a leafSum function to compute the sum of all elements in the leaves, using pattern matching to differentiate between numbers and lists.

def leafSum(tree: List[Any]): Int = tree match {
  case (head: Int) :: tail => head + leafSum(tail)
  case (head@List(_*)) :: tail => leafSum(head) + leafSum(tail)
  case Nil => 0
}

val tree = List(List(3, 8), 2, List(5))
leafSum(tree)

// # Exercise 6
//> A better way of modeling such trees is with case classes. Let’s start with binary trees.  Write a function to compute the sum of all elements in the leaves.

sealed abstract class BinaryTree

case class Leaf(value: Int) extends BinaryTree

case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree

def leafSum(tree: BinaryTree): Double = tree match {
  case Leaf(value) => value
  case Node(left, right) => leafSum(left) + leafSum(right)
}

val binTree = Node(Node(Leaf(3), Leaf(8)), Node(Leaf(2), Leaf(5)))
leafSum(binTree)

//> 18

sealed abstract class ExpressionTree {
  def eval(): Int = this match {
    case o: Operand => o.value
    case Expression(left, op, right) => op(left.eval, right.eval)
  }
}

case class Operand(value: Int) extends ExpressionTree

case class Expression(left: ExpressionTree, oper: (Int, Int) => Int, right: ExpressionTree) extends ExpressionTree

val expr = Expression(
  Expression(Operand(3), _ * _, Operand(8)),
  _ + _,
  Expression(Operand(2), _ + _, Operand(-5))) // (3 × 8) + 2 + (–5) = 21.

expr.eval //> 21

// # Exercise 8
//>  Extend the tree in the preceding exercise so that each nonleaf node stores an operator in addition to the child nodes. Then write a function eval that computes the value.


// # Exercise 9
// > Write a function that computes the sum of the non-None values in a List[Option[Int]]. Don’t use a match statement.

def nonNoneInList(lst: List[Option[Int]]): List[Int] = lst.flatten

// # Exercise 10
// > Write a function that composes two functions of type Double => Option[Double], yielding another function of the same type. The composition should yield None if either function does. For example,

def f(x: Double) = if (x >= 0) Some(Math.sqrt(x)) else None
def g(x: Double) = if (x != 1) Some(1 / (x - 1)) else None

def compose(f: Double => Option[Double], g: Double => Option[Double]):
Double => Option[Double] =
  f(_) match {
    case Some(x) => g(x)
    case None => None
  }

val h = compose(f, g)
h(10) //> Some(0.4624752955742643)
h(0) //> Some(-1.0)
h(-1) //> None
h(1) //> None
