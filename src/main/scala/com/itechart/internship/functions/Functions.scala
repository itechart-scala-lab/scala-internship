package com.itechart.internship.functions

import java.time.Instant

object Functions {

  // Functions are expressions that have parameters, and take arguments.

  // Functions are first-class values:
  // a functions can be assigned to a value, passed as a parameter and returned as a result

  // first order functions take and return ordinary data types
  // higher order functions take and/or return other functions

  // Example.
  def clean(message: String): String = message.replaceAll("fox", "***")

  def mkUpperCase(message: String): String = message.toUpperCase

  // Pass our logic as a parameter `f`
  def processText(message: String, f: String => String): String = f.apply(message)

  def clean2(message: String): String = {
    // `s` is a parameter and may be omitted
    val f: String => String = s => s.replaceAll("fox", "***")
    processText(message, f)
  }

  def mkUpperCase2(message: String): String = {
    val f: String => String = _.toUpperCase
    processText(message, f)
  }

  // Exercise.
  // Implement `isEven` a function that checks if a number is even
  def isEven(x: Int): Boolean = ???

  // Implement `isEvenVal` which behaves exactly like `isEven`.
  // val isEvenVal: Int => Boolean = ???

  // Implement `isEvenDefToVal` by transforming `isEven` def function into a val
  // val isEvenDefToVal: Int => Boolean = ???

  // --

  // Functions can be defined with the following syntax:
  //
  // val functionName: (Parameter1Type, Parameter2Type) => ReturnType = (parameter1: Parameter1Type, parameter2: Parameter2Type) => {
  //  // implementation code goes here
  // }
  //
  // Note that the `: (Parameter1Type, Parameter2Type) => ReturnType` part is the type annotation and can
  // often be skipped as it is inferred by the compiler.
  //

  // If each argument of a function is used exactly once, you can use `_` to refer to them
  val addFunction: (Int, Int) => Int = _ + _

  // First occurrence of _ - it's 1st argument.
  // Second occurrence of _ - it's 2nd argument.
  // And etc...

  // addFunction can be rewritten as:
  val addFunctionExpanded: (Int, Int) => Int = (x, y) => x + y

  // Higher order functions
  //
  // Functions are first class citizens and can be passed as parameters to other functions, as well as
  // returned as return values from functions.

  // Example of a function returned as a return value:
  def greeter(intro: String): String => String = { name: String =>
    s"$intro, $name!"
  }

  val hello: String => String = greeter("Hello")
  val helloWorld: String = hello("World") // Hello, World!

  val goodMorning: String => String = greeter("Good morning")
  val goodMorningWorld: String = goodMorning("World") // Good morning, World!

  // A more convoluted example:
  def formatNamedDouble(name: String, format: Double => String): Double => String = { x: Double =>
    s"$name = ${format(x)}"
  }

  val fourDecimalPlaces: Double => String = (x: Double) => f"$x%.4f"
  val formattedNamedDouble: String = formatNamedDouble("x", fourDecimalPlaces)(Math.PI) // x = 3.1416

  // Exercise. Implement `power` method which takes a Byte `n` and returns a function from Int to
  // Long, raising the Int parameter provided to the n-th power using `Math.pow`.
  // For conversions, use `Double#round` (for rounding Double-s to Long-s) as well as `Byte` and `Int`
  // `toDouble` (for converting Byte-s and Int-s to Double-s).

  def power(n: Byte): Int => Long = { x: Int =>
    // implement here
    (x + n).toLong
  }

  // Polymorphic methods, or methods which take type parameters
  //
  // Methods in Scala can be parameterised by types of their arguments and return values. Type parameters are
  // enclosed in square brackets (in contrast with value parameters which are enclosed in parentheses).
  //
  // The function `formatNamedDouble` can be rewritten in a more general way as follows:

  def formatNamedValue[A](name: String, format: A => String): A => String = { x : A =>
    s"$name = ${format(x)}"
  }

  // Using such "parametric polymorphism" helps us do "parametric reasoning" - to reason about implementation
  // merely by looking at type signatures.

  // Using type parameters hides information from the implementation of the function. Hiding information
  // reduces the number of possible implementations, which makes code easier to understand and reuse.

  // Thus, while initially parametric polymorphisms seems to make our code more complicated, as you gain
  // experience with it, it will often help you write simpler, more maintainable code.

  val commasForThousands: Long => String = (x: Long) => f"$x%,d"
  val formattedLong: String = formatNamedValue("y", commasForThousands)(123456) // y = 123,456

  // Question: What is `A` for `formatNamedValue` in this `formattedLong` invocation of it?

  // Exercise. Invoke `formatNamedValue` with a `List[String]` as `A`. You can use `_.mkString(", ")` to
  // concatenate the list with comma as a delimiter. You can provide the `List[String]` type
  // explicitly after the method name or for the `format` function.


  // In Scala, every concrete type is a type of some class or trait
  // `(String => String)` is the same as scala.Function1[String, String]
  // `scala.Function1[A, B]` is a trait, where `A` and `B` are type parameters

  // an instance of a function can be treated as object

  // The simplified version of the scala.Function1
  object Functions {
    trait Function1[T, R] {
      // `apply` defines how we transform `T` to `R`
      def apply(v1: T): R
    }
  }

  // More common way to define a function type is just `A => B`
  // `A => B` is the type of a function that takes an arg of type A and return a result of type B

  // Syntax sugar allows to call a function w/o typing `apply`
  // `f.apply(..)` becomes `f(..)`

  // We can write a function w/o giving a name
  processText("some text", _ + "!!")

  // Anonymous function expands to implementation of scala.Function1 trait
  processText("some text", new Function1[String, String] {
    override def apply(v1: String): String = v1 + "!!"
  })

  // Method can be passed as a function, but it is not a function value, it's just converted automatically
  def trimAndWrap(v: String): String = s"<${v.trim}>"

  processText("xxx", trimAndWrap)


  // Subclassing Functions
  // One nice aspect of functions being traits is that we can subclass the function type

  trait MyMap[K, V] extends (K => V)

  // Question. What should we extend to ..

  // check if an element belongs to a set
  // > trait MySet[A] extends ???

  // return a value by its index
  // > trait MySeq[A] extends ???

  // --

  // Polymorphic functions has at least one type parameter
  // A type parameter is a form of encapsulation

  def x[T](v: T) = ???

  // Exercise.
  // Implement `mapOption` a function. Do not use scala option api
  def mapOption[A, B](option: Option[A], f: A => B): Option[B] = ???

  // --

  // Functions composition

  val strToInt: String => Int = _.length
  val intToBool: Int => Boolean = _ > 10

  val strToBool1: String => Boolean = t => intToBool(strToInt(t))
  val strToBool2: String => Boolean = intToBool.compose(strToInt)
  val strToBool3: String => Boolean = strToInt.andThen(intToBool)

  // --

  // The pattern matching block expands to the Function1 instance
  val pingPong: String => String = {
    case "ping" => "pong"
  }

  // Question. What happens next?
  // > pingPong("hi?")

  // With the function type itself we cannot find out beforehand
  // whether the function is applicable to a certain argument

  // Partial functions is another trait which extends Function and has `isDefinedAt` method

  val pingPongPF: PartialFunction[String, String] = {
    case "ping" => "pong"
  }

  pingPongPF.isDefinedAt("ping") // > true
  pingPongPF.isDefinedAt("hi") // > false


  // If expected type is a PF then a pattern matching block will expended to PF implementation

  val pingPongPFImpl: PartialFunction[String, String] = new PartialFunction[String, String] {
    override def isDefinedAt(x: String): Boolean = x match {
      case "ping" => true
      case _ => false
    }

    override def apply(v: String): String = v match {
      case "ping" => "pong"
    }
  }

  // Example of using partial functions:
  val eithers: Seq[Either[String, Double]] = List("123", "456", "789o")
    .map(x => x.toDoubleOption.toRight(s"Failed to parse $x"))

  val errors: Seq[String] = eithers.collect {
    case Left(x) => x
  }

  // We can make a function that returns another function
  // Example.
  type Language = String

  def translate(message: String, from: Language, to: Language): String = {
    // some logic
    if (from == to) message else message.reverse
  }

  val translateFromRus: (Language, String) => String =
    (to: String, message: Language) => translate(message, "rus", to)

  // `=>` has right associative law
  val translateF: Language => (Language => (String => String)) =
    (from: Language) => (to: Language) => (message: String) => translate(message, from, to)

  val fromRu = translateF("ru")
  val fromRuToEn = fromRu("en")
  val result = fromRuToEn("функция")

  // TODO: add tailrec

  // Multiple parameter lists ~ syntax sugar for functions returning a function
  def translateM(from: Language)(to: Language)(message: String): String = translate(message, from, to)

  // --

  // Pure functions are mappings between two sets

  // A function is impure if ..
  // - is not defined for all values of input type
  // - throws an exception
  // - returns a value that depends on something else than an input value
  // - works with mutable shared state
  // - does something that is not present in the function signature (side effects)
  // - relies on reflection

  // Why is Null bad?
  // null causes NullPointerException
  // null cannot be removed from the language (although Scala 3 will help handle it)
  // `null` can be passed anywhere

  // Exercise. Provide an example of an impure functions

  // Is `plus` a pure function? Why?
  def plus(a: Int, b: Int): Int = a + b

  // Is `mapLookup` a pure function? Why?
  def mapLookup(map: Map[String, Int], key: String): Int =
    map(key)

  // Pure function should:
  // - be deterministic
  // - not have side effects
  // - be total (not partial)
  // - not throw exceptions
  // - not do any mutation (local, non-local, reference, etc.)
  // - not use `null`

  // A function without side effects only returns a value


  // Exercise. Provide an example of pure functions
  // Question. If a function return for all inputs the same value, is this function pure?

  // Benefits of pure functions

  // Fearless refactoring: any value can be replaced by the function that produced it (referential transparency)
  // Documentations based on functions types
  // Easier to test: no mutation, no randomness, no side effects
  // Potential compiler optimisations
  // Make parallel processing easier


  // Exercises. Convert the following function into a pure function.
  type ??? = Nothing // just to make it compile and indicate that return type should be changed

  //
  def parseDate(s: String): Instant = Instant.parse(s)
  def parseDatePure(s: String): ??? = ???

  //
  def divide(a: Int, b: Int): Int = a / b
  def dividePure(a: Int, b: Int): ??? = ???

  //
  def isAfterNow(date: Instant): Boolean = date.isAfter(Instant.now())
  def isAfterNowPure(/* ??? */): Boolean = ???

  //
  case class Nel[T](head: T, rest: List[T])
  def nel[T](list: List[T]): Nel[T] = {
    if (list.isEmpty) println("ERROR: provide non empty list")
    Nel(list.head, list.tail)
  }
  def nelPure[T](list: List[T]): ??? = ???

  // --

  // Additional exercises:
  // https://www.scala-exercises.org/std_lib/higher_order_functions
  // https://www.scala-exercises.org/fp_in_scala/getting_started_with_functional_programming
}
