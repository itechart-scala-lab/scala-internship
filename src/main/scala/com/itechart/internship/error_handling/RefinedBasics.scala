package com.itechart.internship.error_handling

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{Even, Negative, NonNegative, Odd, Positive}
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.{refineV, W}

object RefinedBasics {

  // SBT:
  // libraryDependencies += "eu.timepit" %% "refined" % "0.9.15"

  // Refined is a Scala library for refining types with type-level predicates
  // which constrain the set of values described by the refined type

  // Very useful for type level validation

  val positiveInt: Refined[Int, Positive] = 42
  // val negativeInt: Refined[Int, Positive] = -100 // will fail

  /* You saw the numerical Positive predicate. There are tons of others:

   - allow only negative numbers
   - allow only non-negative numbers (including 0)
   - allow only odd numbers
   - allow only even numbers

   */

  // Notice I used Refined in infix notation:
  // Refined[Int, Odd] can also be written as Int Refined Odd

  val negative:    Int Refined Negative    = -100
  val nonNegative: Int Refined NonNegative = 0
  val anOdd:       Int Refined Odd         = 3
  val anEven:      Int Refined Even        = 68

  // should contain alphabetic first name and last name in format `XXX YYY`. Example: "Alexander Ovechkin"
  type Owner = String Refined MatchesRegex[W.`"^(([A-Za-z]+ ?){1,3})$"`.T]

  // should contain 16 digits in format `XXXXXXXXXXXXXXXX`. Example: "1234567890123456"
  type CardNumber = String Refined MatchesRegex[W.`"^[0-9]{16}$"`.T]

  // should contain 3 digits
  type SecurityCode = String Refined MatchesRegex[W.`"^[0-9]{3}$"`.T]

  // valid scenarios

  val name         = "Alexander Ovechkin"
  val cardNumber   = "1234567890123456"
  val securityCode = "998"

  val r1: Either[String, Owner]        = refineV(name)
  val r2: Either[String, CardNumber]   = refineV(cardNumber)
  val r3: Either[String, SecurityCode] = refineV(securityCode)

  val invalidName = "Invalid 1"
  val r4: Either[String, Owner] = refineV(invalidName)

  val invalidCardNumber = "123456780123456"
  val r5: Either[String, CardNumber] = refineV(invalidCardNumber)

  def main(args: Array[String]): Unit = {}

  // More links:
  // https://www.youtube.com/watch?v=IDrGbsupaok
  // https://github.com/fthomas/refined
}
