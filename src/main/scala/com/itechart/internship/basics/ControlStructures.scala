package com.itechart.internship.basics

import scala.annotation.tailrec

object ControlStructures {

  // The if-then-else construct is as follows:
  //
  // val result =
  //   if (boolean1) {
  //     result1
  //   } else if (boolean2) {
  //     result2
  //   } else {
  //     otherResult
  //   }
  //
  // Note that it returns a result value.

  // Note that curly braces can be omitted
  //  val result =
  //    if (boolean1) result1
  //    else if (boolean2) result2
  //    else otherResult

  // In scala there is no ternary operator

  // val ternaryResult1 = (1999 / 4 == 500) ? "right": "wrong!"
  val ternaryResult2 = if (1999 / 4 == 500) "right" else "wrong!"

  // Exercise 1. Implement a "Fizz-Buzz" https://en.wikipedia.org/wiki/Fizz_buzz function using the if-then-else,
  // returning "fizzbuzz" for numbers which divide with 15, "fizz" for those which divide by 3 and "buzz" for
  // those which divide with 5, and returning the input number as a string for other numbers:
  def fizzBuzz1(n: Int): String = ???

  // Pattern Matching
  //
  // Using the match-case construct we can write constructs equivalent to if-then-else statements in, often,
  // a more readable and concise form:
  //
  // val result = someValue match {
  //    case pattern1                       => result1
  //    case pattern2 if (guardCondition)   => result2
  //    case _                              => fallbackResult
  // }

  type ErrorMessage = String
  def monthName(x: Int): Either[ErrorMessage, String] =
    x match {
      case 1           => Right("January")
      case 2           => Right("February")
      case 3           => Right("March")
      case 4           => Right("April")
      case 5           => Right("May")
      case 6           => Right("June")
      case 7           => Right("July")
      case 8           => Right("August")
      case 9           => Right("September")
      case 10          => Right("October")
      case 11          => Right("November")
      case 12          => Right("December")
      case x if x <= 0 => Left(s"Month $x is too small")
      case x           => Left(s"Month $x is too large")
    }

  // Question. How would you improve `monthName`?
  // Question. What would you use in its place if you wanted to more properly handle multiple locales?

  def monthNameEnhanced(x: Int): Either[ErrorMessage, String] = {
    val months = Map(
      1  -> "January",
      2  -> "February",
      3  -> "March",
      4  -> "April",
      5  -> "May",
      6  -> "June",
      7  -> "July",
      8  -> "August",
      9  -> "September",
      10 -> "October",
      11 -> "November",
      12 -> "December",
    )
    x match {
      case x if x <= 0          => Left(s"Month $x is too small")
      case x if x > months.size => Left(s"Month $x is too large")
      case x                    => Right(months(x))
    }
  }

  // the better solution will be with Scala ADT / enumeration

  println(monthNameEnhanced(1))
  println(monthNameEnhanced(12))
  println(monthNameEnhanced(15))

  sealed trait Shape

  object Shape {
    case object Origin extends Shape
    final case class Circle(radius: Double) extends Shape
    final case class Rectangle(width: Double, height: Double) extends Shape
  }

  import Shape._

  // Typed Pattern
  def matchOnShape1(s: Shape): String = s match {
    case Origin => s"Found the origin."
    case circle:    Circle    => s"Found a circle $circle."
    case rectangle: Rectangle => s"Found a rectangle $rectangle."
  }

  // Unapply the instance of Shape
  def matchOnShape2(s: Shape): String = s match {
    case Origin                   => s"Found the origin."
    case Circle(radius)           => s"Found a circle with radius $radius."
    case Rectangle(width, height) => s"Found a rectangle with width $width and height $height."
  }

  def matchOnShape3(s: Shape): String = s match {
    case Origin => s"Found the origin."
    case circle    @ Circle(radius)           => s"Found a circle $circle with radius $radius."
    case rectangle @ Rectangle(width, height) => s"Found a rectangle $rectangle with width $width and height $height."
  }

  // Exercise 2. Implement a "Fizz-Buzz" function using pattern matching:
  def fizzBuzz2(n: Int): String = ???

  // Recursion
  //
  // A function which calls itself is called a recursive function. This is a commonly used way how to
  // express looping constructs in Functional Programming languages.

  def sum1(list: List[Int]): Int =
    if (list.isEmpty) 0
    else list.head + sum1(list.tail)

  val s1 = sum1(List(1, 2, 3, 4, 5))
  val s2 = sum1(List(1))
  val s3 = sum1(List.empty[Int])

  // Question. What are the risks of recursion when applied without sufficient foresight?

  // @tailrec annotation verifies that a method will be compiled with tail call optimisation.

  // @tailrec
  def factorial(n: Int): Either[ErrorMessage, Int] = {

    @tailrec
    def factorialAcc(acc: Int, n: Int): Int =
      if (n <= 1) acc
      else factorialAcc(n * acc, n - 1)

    if (n < 0) Left(s"Negative number doesn't have factorial!")
    else Right(factorialAcc(1, n))
  }

  println(factorial(5))

  @tailrec
  def last[A](list: List[A]): Option[A] = list match {
    case Nil      => None
    case x :: Nil => Some(x)
    case _ :: xs  => last(xs)
  }

  // Recursion isn't used that often as it can be replaced with `foldLeft`, `foldRight`,
  // `reduce` or other larger building blocks.

  def sum2(list: List[Int]): Int =
    list.foldLeft(0)((acc, x) => acc + x)

  def sum3(list: List[Int]): Int =
    list.foldRight(0)((x, acc) => acc + x)

  def sum4(list: List[Int]): Int =
    if (list.isEmpty) 0
    else list.reduce((a, b) => a + b)

  def sum5(list: List[Int]): Int =
    list.sum // only for Numeric lists

  // `map`, `flatMap` and `filter` are not control structures, but methods that various collections (and
  // not only collections) have. We will discuss them now as they are important to understand a key
  // control structure called "for comprehensions".

  // `map` is a higher order function which - in case of collections - transforms each element of the
  // collection into a different element (and returns the resulting collection)

  // Question. What is the value of this code?
  val listMapExample = List(1, 2, 3).map(x => x * 2)

  // As we will see in later lessons, `map` is a method that `Functor`-s have, and there are more `Functor`-s
  // than just collections (`IO`, `Future`, `Either`, `Option` are all `Functor`-s too).

  // For now, we will have a utilitarian focus and not go into `Functor`-s and other type classes.

  // `flatMap` is a higher order function which - for collections - transforms each element of the collection
  // into a collection, and then `flatten`-s these collections.

  // Question. What is the value of this code?
  val listFlatMapExample = List(1, 2, 3).flatMap(x => List(x * 2))

  // Question. Do you think only collections can have `flatMap`?

  // `filter` takes a predicate function returning a boolean and - for collections - returns a collection
  // with only these elements which satisfy this predicate.

  // Question. What is the value of this code?
  val listFilterExample = List(1, 2, 3).filter(_ % 2 == 0)

  // For Comprehensions

  // A `for-yield` syntax is syntactic sugar for composing multiple `map`, `flatMap` and `filter` operations
  // together in a more readable form.

  // val result = for {
  //   x <- a
  //   y <- b
  // } yield x + y
  //
  // gets translated to
  //
  // val result = a.flatMap(x => b.map(y => x + y))

  private val a = List(1, 2, 3)
  private val b = List(10, 100)

  val c = for {
    x <- a
    y <- b
  } yield x * y

  val d = a.flatMap(x => b.map(y => x * y))

  // Question: What is the value of `c` above?
  // Question: What is the value of `d` above?

  // You can also add `if` guards to `for` comprehensions:
  val e = for {
    x <- a // generator
    z  = x % 2 // definition
    if z == 1 // filter expression
    y <- b // generator
  } yield x + y

  // Question. What is the value of `e` above?

  // In idiomatic functional Scala, much of the code ends up written in "for comprehensions".
  // Exercise. Implement `makeTransfer` using `for` comprehensions and the methods provided in `UserService`.

  type UserId = String
  type Amount = BigDecimal

  trait UserService {
    def validateUserName(name: String): Either[ErrorMessage, Unit]
    def findUserId(name:       String): Either[ErrorMessage, UserId]
    def validateAmount(amount: Amount): Either[ErrorMessage, Unit]
    def findBalance(userId:    UserId): Either[ErrorMessage, Amount]

    /** Upon success, returns the resulting balance */
    def updateAccount(userId: UserId, previousBalance: Amount, delta: Amount): Either[ErrorMessage, Amount]
  }

  // Upon success, should return the remaining amounts on both accounts as a tuple.
  def makeTransfer(
    service:          UserService,
    fromUserWithName: String,
    toUserWithName:   String,
    amount:           Amount
  ): Either[ErrorMessage, (Amount, Amount)] = {
    // Replace with a proper implementation that uses validateUserName on each name,
    // findUserId to find UserId, validateAmount on the amount, findBalance to find previous
    // balances, and then updateAccount for both userId-s (with a positive and negative
    // amount, respectively):
    println(s"$service, $fromUserWithName, $toUserWithName, $amount")
    ???
  }

  /** How to write a class that can be used in a for expression?
    *
    *  The Scala compiler converts the for expressions you write into a series of method calls.
    *  These calls may include map, flatMap, foreach, and withFilter.
    *
    *  Book "Programming in Scala" gives us these translation rules:
    *    1. If a custom data type defines a foreach method, it allows for `loops` (both with single and multiple generators).
    *    2. If a data type defines only map, it can be used in `for expressions` consisting of a single `generator`.
    *    3. If it defines `flatMap` a well as `map`, it allows `for expressions` consist of multiple `generators`.
    *    4. If it defines `withFilter`,it allows for `filter expressions` starting with an `if` within the `for expression`.
    */

  abstract class CustomClass[A] {
    // allows single generator
    def map[B](f: A => B): CustomClass[B]

    // with map allows multiple generator
    def flatMap[B](f: A => CustomClass[B]): CustomClass[B]

    // allows filters in `for yield`
    def withFilter(p: A => Boolean): CustomClass[A]

    // allows loops
    def foreach(b: A => Unit): Unit
  }

  def main(args: Array[String]): Unit = {}
}
