package com.itechart.internship.basics

import scala.util.{Failure, Success, Try}

object TestTaskHomework {

  // Homework
  //
  // Revisit your test task for internship
  // Try to do your best in order to implement this task with:
  // - immutable values, objects
  // - creating a type hierarchy with traits, classes or case classes
  // - using Match & For expression
  // - without throwing exceptions - use Either, Option
  // Our goal is to make the code as clean and functional as possible!

  // Question. How to parse objects from string?

  // In idiomatic functional Scala, instead of throwing exceptions other error handling mechanisms are usually used.
  // Throwing exceptions is an anti-pattern - it introduces another exit path from a function and breaks
  // referential transparency.
  // It can be thought of as a "`goto` to an unknown place up the call stack".

  // One of these other mechanisms is `Try[A]` which can be thought of as an `Either[Throwable, A]`:

  def parseInt(x: String): Try[Int] = Try(x.toInt)

  def main(args: Array[String]): Unit = {
    parseInt("10") match {
      case Success(value) => println(value)
      case Failure(error) => println(error)
    }

    parseInt("asdf") match {
      case Success(value) => println(value)
      case Failure(error) => println(error)
    }
  }
}
