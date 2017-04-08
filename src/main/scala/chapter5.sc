// # Scala For The Impatient -- Chapter 4

// ## Exercise 1
// > Improve the Counter class in Section 5.1 so that it doesn’t turn negative at Int.MaxValue.

// We can  use `Long` (or even `BigInt`) instead of `Int` :
// ```
// private var value : Long = 0
// ```
// We can also add a _guard_ to check for max value...
class Counter {
  private var value = 0

  // You must initialize the field
  def increment() {
    if (value < Int.MaxValue) value += 1
  } // Methods are public by default def current() = value
}

// ## Exercise 2
// > Write a class BankAccount with methods deposit and withdraw, and a read-only property balance.

class BankAccount {
  // We could also restrict the access to this instance of `BankAccount`
  // using `private[this] var _balance`.
  private var _balance = 0

  def deposit(amount: Int) = _balance += amount

  def withdraw(amount: Int) =
    if (amount > _balance) sys.error("You don't have the cash!")
    else _balance -= amount
}

// ## Exercise 3
// > Write a class Time with read-only properties hours and minutes and a method before(other: Time): Boolean that checks whether this time comes before the other. A Time object should be constructed as new Time(hrs, min), where hrs is in military time format (between 0 and 23).

// Use `val` in constructor arguments to make them immutable.
// As a reminder,
// > If a parameter without val or var is used inside at least one method, it becomes a field.
//
{
  class Time(val hours: Int, val minutes: Int) {
    // use `require` to check the arguments' validity
    require(hours >= 0 && hours <= 23)
    require(minutes >= 0 && minutes <= 60)

    def before(other: Time): Boolean =
      hours < other.hours ||
        (hours == other.hours && minutes < other.minutes)

    override def toString = s"Time($hours:$minutes)"
  }

  val t1 = new Time(10, 23)
  println(t1 before new Time(10, 22)) //> false
  println(t1 before new Time(11, 0)) //> true
  // new Time(24, 70) //> IllegalArgumentException: requirement failed

}

// ## Exercise 4
// > Reimplement the Time class from the preceding exercise so that the internal representation is the number of minutes since midnight (between 0 and 24 × 60 – 1). Do not change the public interface. That is, client code should be unaffected by your change.

{
  // Now, a `Time` object only has a `minutes` field
  class Time(val minutes: Int) {
    require(minutes >= 0 && minutes <= 24 * 60 - 1)

    // Define a secondary constructor following the previous
    // `Time` implementation's interface
    def this(hours: Int, minutes: Int) = this((hours * 60) + minutes)

    def before(other: Time): Boolean =
      minutes < other.minutes

    override def toString = s"Time($minutes)"
  }

  val t1 = new Time(10, 23)
  println(t1 before new Time(10, 22)) //> false
  println(t1 before new Time(11, 0)) //> true
  // new Time(24, 70) //> IllegalArgumentException: requirement failed
}

// ## Exercise 5
// > Make a class Student with read-write JavaBeans properties name (of type String) and id (of type Long). What methods are generated? (Use javap to check.) Can you call the JavaBeans getters and setters in Scala? Should you?

// Using `javap`, here is the java class:
// ```
// Compiled from "Lala.scala"
// public class Student {
//   public long id();
//   public void id_$eq(long);
//   public java.lang.String name();
//   public void name_$eq(java.lang.String);
//   public long getId();
//   public void setId(long);
//   public java.lang.String getName();
//   public void setName(java.lang.String);
//   public Student(long, java.lang.String);
// }
// ```
// notice how get get both the scala accessors (`id_$eq`, etc) and the bean getters/setters.
//
// _Note_: if your replace `var id` by `val id`, only the getter is generated.

import scala.beans.BeanProperty

class Student(@BeanProperty var id: Long, @BeanProperty var name: String) {}

// Yes, getters and setters are callable from Scala
var s1 = new Student(0, "Derlin")
s1.getId //> 0

// We can use scala accessors and bean getters/setters interchangeably
s1.id == s1.getId //> true
s1.name == s1.getName //> true

s1.setName("Lucy")
s1.name = "Lucy Derlin"
s1.getName

// ## Exercise 6
// > In the Person class of Section 5.1, “Simple Classes and Parameterless Methods,” on page 51, provide a primary constructor that turns negative ages to 0.

// If the `age` field is a val, one way to go is to make the `age` in the constructor a regular argument and create the `age`
// field in the constructor body.
{
  class Person(name: String, val anAge: Int) {
    val age = anAge min 0
  }
}

// If the `age` field is a var, it is easier
// field in the constructor body.
{
  class Person(name: String, var age: Int) {
    age = age min 0
  }
}

// ## Exercise 7
// > Write a class Person with a primary constructor that accepts a string containing a first name, a space, and a last name, such as new Person("Fred Smith"). Supply read-only properties firstName and lastName. Should the primary constructor parameter be a var, a val, or a plain parameter? Why?

// Without a `var` or `val` prefix and because we don't use the parameter `name` outside of the primary constructor,
// it will stay what it is: a mere parameter.
{
  class Person(name: String) {
    require(name.split(" +").length == 2)
    // In scala, it is possible to use pattern matching in an assignment.
    // Here, the first item of the array becomes the firstName, the second the last name.
    // _Warning_: in case `split` yields an array of length < 2, a `scala.MatchError` is generated.
    val Array(firstName, lastName) = name.split(" +")


  }

  val p = new Person("Lucy Derlin")
  println(s"firstname: ${p.firstName}, lastname: ${p.lastName} ")
  // p.name does not exist
}


// ## Exercise 8
// > Make a class Car with read-only properties for manufacturer, model name, and model year, and a read-write property for the license plate. Supply four constructors. All require the manufacturer and model name. Optionally, model year and license plate can also be specified in the constructor. If not, the model year is set to -1 and the license plate to the empty string. Which constructor are you choosing as the primary constructor? Why?

// The primary constructor must declare all the properties, for two reasons:
//  1. It is where `var` and `val` can be used (and we need it to make the license plate read/write)
//  2. a secondary constructor _must_ call the primary constructor
{
  class Car(val manufacturer: String, val modelName: String, val modelYear: Int, var licensePlate: String) {

    def this(manufacturer: String, modelName: String, modelYear: Int) = this(manufacturer, modelName, modelYear, "")

    def this(manufacturer: String, modelName: String, licensePlate: String) = this(manufacturer, modelName, -1, licensePlate)

    def this(manufacturer: String, modelName: String) = this(manufacturer, modelName, -1, "")
  }
}

// A more succinct way to go is to use only one constructor with optional arguments:
{
  class Car(val manufacturer: String, val modelName: String, val modelYear: Int = -1, var licensePlate: String = "") {}
  new Car("Renault", "Megane")
  new Car("Renault", "Megane", 1990)
  new Car("Renault", "Megane", 1990, "XXA345")
  new Car("Renault", "Megane", licensePlate = "XXA345") // if year is missing, specify the argument's name
}

// ## Exercise 9
// > Reimplement the class of the preceding exercise in Java, C#, or C++ (your choice). How much shorter is the Scala class?

// In Java, parameters/constructors+getter/setter is about 51 lines ! See `misc/JavaCar`.

// ## Exercise 10
// Consider the class
// ```
// class Employee(val name: String, var salary: Double) {
//     def this() { this("John Q. Public", 0.0) }
// }
// ```
// Rewrite it to use explicit fields and a default primary constructor. Which form do you prefer? Why?

{
  class Employee(val name: String = "John Q. Public", var salary: Double = .0)

  val e1 = new Employee
}