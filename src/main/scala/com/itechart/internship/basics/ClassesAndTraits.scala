package com.itechart.internship.basics

object ClassesAndTraits {

  // Classes in Scala are blueprints for creating object instances. They can contain methods, values,
  // variables, types, objects, traits, and classes which are collectively called members.

  class MovableLocation(var x: Double, var y: Double) {
    def move(dx: Double, dy: Double): Unit = {
      x = x + dx
      y = y + dy
    }

    override def toString: String =
      s"($x, $y)"
  }

  // Singleton objects are defined using `object`.
  // It is a class that has exactly one instance.
  // An object with the same name as a class is called a companion object.
  //
  // Use it to contain methods and values related to this trait or class, but that aren't
  // specific to instances of this trait or class.
  object MovableLocation {
    def apply(x: Double, y: Double): MovableLocation = new MovableLocation(x, y)

    def unapply(point: MovableLocation): Option[(Double, Double)] = Option((point.x, point.y))
  }

  val location1 = new MovableLocation(3, 4)
  println(location1.x) // 3.0
  println(location1) // (3.0, 4.0)

  location1.move(1, 2)
  println(location1) // 4.0, 6.0

  // Question. Is MutablePoint a good design? Why or why not?

  // Traits define a common interface that classes conform to. They are similar to Java's interfaces.

  // A trait can be thought of as a contract that defines the capabilities and behaviour of a component.

  // Subtyping
  // Where a given trait is required, a subtype of the trait can be used instead.

  // Classes and singleton objects can extend traits.
  //
  // This allows "programming to the interface" approach where you depend on traits instead of their
  // specific implementations (classes or objects).
  //
  // This makes code more reusable and testable.

  sealed trait Located {
    def x: Double
    def y: Double
  }

  sealed trait Movable {
    def move(dx: Double, dy: Double): Vehicle
  }

  sealed trait Vehicle extends Located with Movable

  final case class GroundVehicle(x: Double, y: Double, weight: Double) extends Vehicle {
    override def move(dx: Double, dy: Double): Vehicle = GroundVehicle(x + dx, y + dy, weight)
  }

  // Exercise: implement Ship case class which will extend Vehicle and contain new property `displacement`
  // final case class Ship()

  // Case Classes
  //
  // Case classes are like regular classes, but with extra features which make them good for modelling
  // immutable data. They have all the functionality of regular classes, but the compiler generates additional
  // code, such as:
  // - Case class constructor parameters are public `val` fields, publicly accessible
  // - `apply` method is created in the companion object, so you don't need to use `new` to create a new
  //   instance of the class
  // - `unapply` method which allows you to use case classes in `match` expressions (pattern matching)
  // - a `copy` method is generated
  // - `equals` and `hashCode` methods are generated, which let you compare objects & use them in collections
  // - `toString` method is created for easier debugging purposes
  // - extends Serializable
  //
  // Case object provides default equals, hashCode, unapply, toString methods, extends Serializable
  //
  // When declaring a case class, make it final. Otherwise someone may decide to inherit from it
  // case-to-case inheritance is prohibited

  val groundVehicle1 = GroundVehicle(1, 2, 3500)
  println(groundVehicle1.x)

  val groundVehicle2: Vehicle = groundVehicle1
  val groundVehicleDesc = groundVehicle2 match {
    case GroundVehicle(x, y, weight) => s"x = $x, y = $y, weight = $weight kg"
    case _                           => "other vehicle"
  }

  val groundVehicle3 = groundVehicle1.copy(x = 3)
  println(groundVehicle3.toString) // GroundVehicle(3, 2)

  // Pattern matching and exhaustiveness checking

  //  final case class Bike() extends Vehicle {
  //    override def move(dx: Double, dy: Double): Vehicle = ???
  //    override def x: Double = ???
  //    override def y: Double = ???
  //  }

  def describe(x: Vehicle): String = x match {
    case GroundVehicle(x, y, weight) => s"GroundVehicle(x = $x, y = $y, weight = $weight)"
    //case Ship(x, y, displacement)  => s"Ship(x = $x, y = $y, displacement = $displacement)"
  }

  // Singleton can extend classes and mix in traits
  object OriginLocation extends Located {
    override def x: Double = 0
    override def y: Double = 0
  }

  // Generic classes and type parameters

  // In a similar way as we saw with polymorphic methods, classes and traits can also take type parameters.
  // For example, you can define a `Stack[A]` which works with any type of element `A`.

  // Question. Do you agree with how the stack is modelled here? What would you do differently?

  final case class Stack[A](elements: List[A] = Nil) {
    def push(x: A): Stack[A] = ???
    def peek: A             = ???
    def pop:  (A, Stack[A]) = ???
  }

  // TODO: remove before lection
  final case class StackEnhanced[A](elements: List[A] = Nil) {
    def push(x: A): Stack[A] = Stack(x :: elements)
    def peek: Option[A] = elements.headOption

    def pop: Option[(A, Stack[A])] = elements match {
      case Nil     => None
      case x :: xs => Some(x, Stack(xs))
    }
  }

  def main(args: Array[String]): Unit = {}
}
