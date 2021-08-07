package com.itechart.internship.adt

object AlgebraicDataTypes {

  // ALGEBRAIC DATA TYPES

  // Algebraic Data Types or ADTs is a commonly used way of structuring data, used in many programming
  // languages (so this is not something unique to Scala).
  //
  // While the definition may sound scientific and complex, in reality we have already been using this
  // concept in `basics` package. For example, see `sealed trait Shape` in `ClassesAndTraits`. We will now
  // look into the concept and its use cases in more detail.

  // ADTs are widely used in Scala for multiple reasons:
  // - to ensure it is hard or even impossible to represent invalid data;
  // - because pattern matching and ADTs play nicely together.

  // Two common classes of ADTs are:
  // 1. product types: case classes and tuples;
  // 2. sum types: sealed traits and abstract classes.
  // 3. hybrid types: product type + sum type
  // 3. values classes: rarely used (https://docs.scala-lang.org/overviews/core/value-classes.html)

  // PRODUCT TYPES

  // A product type allows to combine multiple values into one. Canonical Scala examples of product types are
  // case classes and tuples. See `Basics` and `ClassesAndTraits` for their introduction.

  // A product type is called like that because one can calculate how many different values it can possibly
  // have by multiplying the number of such possibilities for the types it combines. The resulting number
  // is called the arity or number of operands/args of the product type.

  // Question. What is the arity of the product type `(Boolean, Boolean)`?
  type DoubleBoolean = (Boolean, Boolean)

  final case class DoubleBooleanCaseClass(b1: Boolean, b2: Boolean)

  // Question. What is the arity of the product type `Person`?
  final case class Person(name: String, surname: String, age: Int)

  // Question. `Int`, `Double`, `String`, etc. are useful types from the Scala standard library, which can
  // represent a wide range of data. In the product type `Person`, both the name and the surname are
  // represented by `String`. Is that a good idea?

  /* Arity cheat sheet:

  Nothing has 0 possible values
  Unit has 1 possible values
  Bool has 2 possible values
  Byte has 256 possible values
  Int has 2ˆ32 possible values
  String has infinite possible values

   */

  final case class PolicyModelPK(id: Long)
  case class PolicyModel(
    name:        String, // val by default
    displayName: String,
    description: String,
    version:     Long,
    id:          PolicyModelPK = PolicyModelPK(0L)
  )

  // Best practice: When declaring a case class, make it final (!)
  // Otherwise someone may decide to inherit from it
  // case-to-case inheritance is prohibited
  // NEVER repeat the following at home:
  class AdvancedPolicy(
    name:        String,
    displayName: String,
    description: String,
    namespace:   String, // advanced field
    version:     Long,
    id:          PolicyModelPK = PolicyModelPK(0L)
  ) extends PolicyModel(name, displayName, description, version, id)

  val policyName = "policy1"

  val advancedPolicy1 = new AdvancedPolicy(policyName, policyName, policyName, namespace = "default", version = 1)
  val advancedPolicy2 = new AdvancedPolicy(policyName, policyName, policyName, namespace = "whatever", version = 1)

  println(s"Print advanced policy - $advancedPolicy1")

  val result: Boolean = advancedPolicy1 == advancedPolicy2

  println(s"Do advanced policies instances equal? ${advancedPolicy1 == advancedPolicy2}")

  // SMART CONSTRUCTORS

  // Smart constructor is a pattern, which allows creating only valid instances of a class.

  // To disable creating case classes in any other way besides smart constructor,
  // the following pattern can be used:
  sealed abstract case class Time private (hour: Int, minute: Int)
  object Time {
    def create(hour: Int, minute: Int): Either[String, Time] = (hour, minute) match {
      case (h, _) if h < 0 || h > 23 => Left("Invalid 'hour' arg. It should be between 0 and 23")
      case (_, m) if m < 0 || m > 59 => Left("Invalid 'minute' arg. It should be between 0 and 59")
      case _                         => Right(new Time(hour, minute) {})
    }
  }

  val validTime:     Either[String, Time] = Time.create(2, 24)
  val invalidHour:   Either[String, Time] = Time.create(26, 24)
  val invalidMinute: Either[String, Time] = Time.create(2, -2)

  // Exercise 1. Implement the smart constructor for `Month` that only permits values between 1 and 12 and
  // returns "Invalid month value, it must be between 1 and 12" string in `Left` when appropriate.

  object Month {
    def fromInt(value: Int): Nothing = ???
  }

  // Question. Is using `String` to represent `Left` a good idea?

  // SUM TYPES

  // A sum type is an enumerated type. To define it one needs to enumerate all its possible variants.
  // A custom boolean type `Bool` can serve as a canonical example.
  sealed trait Bool
  final case object True extends Bool
  final case object False extends Bool

  // Note that sealed keyword means that `Bool` can only be extended in the same file as its declaration.
  // Question. Why do you think sealed keyword is essential to define sum types?

  // A sum type is called like that because one can calculate how many different values it can possibly
  // have by adding the number of such possibilities for the types it enumerates. The resulting number
  // is called the arity of the sum type.

  sealed trait Weather // Sum type
  object Weather {
    case object Sunny extends Weather
    case object Windy extends Weather
    case object Rainy extends Weather
    case object Cloudy extends Weather
  }

  // Question. What is the arity of the sum type `Weather`?

  import Weather._
  def feeling(weather: Weather): String = weather match {
    case Sunny  => ":)"
    case Windy  => ":|"
    case Rainy  => ":("
    case Cloudy => "???"
  }

  // hybrid types

  // The power of sum and product types is unleashed when they are combined together. For example, consider a
  // case where multiple different payment methods need to be supported. (This is an illustrative example and
  // should not be considered complete)
  final case class AccountNumber(value: String)
  final case class CardNumber(value: String)
  final case class ValidityDate(month: Int, year: Int)

  sealed trait PaymentMethod
  object PaymentMethod {
    final case class BankAccount(accountNumber: AccountNumber) extends PaymentMethod
    final case class CreditCard(cardNumber: CardNumber, validityDate: ValidityDate) extends PaymentMethod
    final case object Cash extends PaymentMethod
  }

  import PaymentMethod._

  final case class PaymentStatus(value: String)
  trait BankAccountService {
    def processPayment(amount: BigDecimal, accountNumber: AccountNumber): PaymentStatus
  }
  trait CreditCardService {
    def processPayment(amount: BigDecimal, cardNumber: CreditCard): PaymentStatus
  }
  trait CashService {
    def processPayment(amount: BigDecimal): PaymentStatus
  }

  // Exercise 2. Implement `PaymentService.processPayment` using pattern matching and ADTs.
  class PaymentService(
    bankAccountService: BankAccountService,
    creditCardService:  CreditCardService,
    cashService:        CashService,
  ) {
    def processPayment(amount: BigDecimal, method: PaymentMethod): PaymentStatus = ???
  }

  // Advantages
  // 1. Illegal states are NOT representable
  // 2. Highly composable
  // 3. Immutable data structures
  // 4. ADT is just Data, no functionality -> good for structuring our code

  // Exercise. Define an Algebraic Data Type `Car`, which has a manufacturer, a model, a production year,
  // and a license plate number (can contain from 3 to 8 upper case letters and numbers). Use value classes
  // and smart constructors as appropriate.
  type Car = Nothing

  def main(args: Array[String]): Unit = {}

  // Attributions and useful links:
  // https://nrinaudo.github.io/scala-best-practices/definitions/adt.html
  // https://alvinalexander.com/scala/fp-book/algebraic-data-types-adts-in-scala/
  // https://en.wikipedia.org/wiki/Algebraic_data_type
}
