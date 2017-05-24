// # Scala For The Impatient -- Chapter 8

// ## Exercise 1
// > Extend the following BankAccount class to a CheckingAccount class that charges $1 for every deposit and withdrawal.

// Here, we just added a `balance` method to get the current balance as well as a proper `toString` method definition.
class BankAccount(initialBalance: Double) {
  private var _balance = initialBalance

  def deposit(amount: Double) = {
    _balance += amount
    _balance
  }

  def withdraw(amount: Double) = {
    _balance -= amount
    _balance
  }

  def balance() = _balance

  override def toString: String = s"Account{balance = ${_balance}}"
}

class CheckingAccount(initialBalance: Double, val charges: Double) extends BankAccount(initialBalance) {

  override def deposit(amount: Double): Double = {
    super.deposit(amount - charges)
  }

  override def withdraw(amount: Double): Double = {
    super.withdraw(amount + charges)
  }
}

// ## Exercise 2
// > Extend the BankAccount class of the preceding exercise into a class SavingsAccount that earns interest every month (when a method earnMonthlyInterest is called) and has three free deposits or withdrawals every month. Reset the transaction count in the earnMonthlyInterest method.

// parameters:
// * `initialBalance`: the balance to begin with
// * `charges`: amount charged for each non-free transaction
// * `interest`: monthly interest rate (in percentage, between 0 and 1)
class SavingsAccount(initialBalance: Double, charges: Double, val interest: Double) extends BankAccount(initialBalance) {
  // Ensure interest is a percentage
  require(interest >= 0 && interest <= 1)

  val maxFreeActions = 3

  // don't let other modify the actions counter
  private[this] var actionsInMonth = 0

  def earnMonthlyInterest() = {
    actionsInMonth = 0
    super.deposit(balance * interest)
  }

  override def deposit(amount: Double): Double = {
    actionsInMonth += 1
    super.deposit(amount - (if (actionsInMonth > maxFreeActions) charges else 0))
  }

  override def withdraw(amount: Double): Double = {
    actionsInMonth += 1
    super.withdraw(amount + (if (actionsInMonth > maxFreeActions) charges else 0))
  }
}

// Create an account with a 10% interest rate, 5$ of charges and 100$ as initial balance.
// Then, play a bit with it
val account = new SavingsAccount(100, 5, 0.1) //> balance = 100
account.deposit(10) //> balance = 110
account.deposit(10) //> balance = 120
account.withdraw(20) //> balance = 100
// Here, we should have 100$ again. The actions are now charged.
account.withdraw(5) //> balance = 90
account.deposit(15) //> balance = 100
account.earnMonthlyInterest()

//> balance = 110


// ## Exercise 3
// > Consult your favorite Java or C++ textbook that is sure to have an example of a toy inheritance hierarchy, perhaps involving employees, pets, graphical shapes, or the like. Implement the example in Scala.
//
// ```
//         +----------+
//         |    os    |
//         +----------+
//           |      |
//  +--------v-+  +-v--------+
//  | windows  |  |   unix   |
//  +----------+  +----------+
//                  |       |
//         +--------v--+   +v------+
//         |  solaris  |   | linux |
//         +-----------+   +-------+
//                          |     |
//                  +-------v-+  +v---------+
//                  | ubuntu  |  |  redhat  |
//                  +---------+  +----------+
//
// ```
// My answer does not do much, but I was tired after the ascii art ^^.

class OS {}

class Windows extends OS {}

class Unix extends OS {}

class Solaris extends Unix {}

class Linux extends Unix {}

class Ubuntu extends Linux {}

class RedHat extends Linux {}


// ## Exercise 4
// > Define an abstract class Item with methods price and description. A SimpleItem is an item whose price and description are specified in the constructor. Take advantage of the fact that a val can override a def. A Bundle is an item that contains other items. Its price is the sum of the prices in the bundle. Also provide a mechanism for adding items to the bundle and a suitable description method.

abstract class Item {
  def price: Double

  def description: String
}

class SimpleItem(override val price: Double, override val description: String) extends Item {}

class Bundle(initialItems: SimpleItem*) extends Item {
  val items: collection.mutable.Buffer[SimpleItem] = initialItems.toBuffer

  def price = items.map(_.price).sum

  def description = items.map(_.description).mkString("+")

  def add(item: SimpleItem) = items.append(item)
}

val b = new Bundle(new SimpleItem(3.5, "Coffee"), new SimpleItem(2, "Cookie"))
s"${b.description} is ${b.price}$$"  //> Coffee+Cookie is 5.5$

