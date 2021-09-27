package com.itechart.internship.tf

import cats.implicits._
import cats.data.Kleisli

object KleisliWrapper {

  //  One of the most useful properties of functions is that they compose.
  //  That is, given a function A => B and a function B => C, we can combine them
  //  to create a new function A => C. It is through this compositional property
  //  that we are able to write many small functions and compose them together
  //  to create a larger one that suits our needs.

  val twice: Int => Int =
    x => x * 2

  val countCats: Int => String =
    x => if (x == 1) "1 cat" else s"$x cats"

  val twiceAsManyCats1: Int => String =
    twice andThen countCats // equivalent to: countCats compose twice

  val x = 2
  val result1: String = countCats(twice(x))
  val result2: String = twiceAsManyCats1(x)

  // Sometimes, our functions will need to return monadic values.
  // For instance, consider the following set of functions

  val parse: String => Option[Int] =
    s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None

  val reciprocal: Int => Option[Double] =
    i => if (i != 0) Some(1.0 / i) else None

//  val parseAndReciprocal =
//    parse andThen reciprocal

  // How deal with this? Kleisli comes into play

  // At its core, Kleisli[F[_], A, B] is just a wrapper around the function A => F[B].
  // Depending on the properties of the F[_], we can do different things with Kleislis.
  // For instance, if F[_] has a FlatMap[F] instance (we can call flatMap on F[A] values),
  // we can compose two Kleislis much like we can two functions.

  val parseKleisli: Kleisli[Option, String, Int] =
    Kleisli((s: String) => if (s.matches("-?[0-9]+")) Some(s.toInt) else None)

  val reciprocalKleisli: Kleisli[Option, Int, Double] =
    Kleisli((i: Int) => if (i != 0) Some(1.0 / i) else None)

  val parseAndReciprocalKleisli: Kleisli[Option, String, Double] =
    parseKleisli andThen reciprocalKleisli

  val raw = "2"
  val resultWithKleisli: Option[Double] = parseAndReciprocalKleisli(raw)

  println()

  // For more details: https://typelevel.org/cats/datatypes/kleisli.html

  def main(args: Array[String]): Unit = {}
}
