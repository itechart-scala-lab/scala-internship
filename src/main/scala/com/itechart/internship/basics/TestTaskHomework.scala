package com.itechart.internship.basics

import scala.util.Try

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

//  def main(args: Array[String]): Unit = {
//    parseInt("10") match {
//      case Success(value) => println(value)
//      case Failure(error) => println(error)
//    }
//
//    parseInt("asdf") match {
//      case Success(value) => println(value)
//      case Failure(error) => println(error)
//    }
//  }

  // possible interface for your solution below (keep in mind that
  // it is not mandatory interface, you can change it if you think
  // it will make your code better and more comfortable to use):

  type ErrorMessage = String
  final case class RawSpreadsheet(/* add here properties */ )
  final case class ProcessedSpreadsheet(/* add here properties */ )

  def validateReadPath(readPath:  String): Either[ErrorMessage, String] = ???
  def validateWritePath(readPath: String): Either[ErrorMessage, String] = ???

  trait SpreadsheetParser {
    def parse(): Either[ErrorMessage, RawSpreadsheet]
  }

  class LocalSpreadsheetParser(path: String) extends SpreadsheetParser {
    override def parse(): Either[ErrorMessage, RawSpreadsheet] = ???
  }

  trait SpreadsheetProcessor {
    def process(spreadsheet: RawSpreadsheet): ProcessedSpreadsheet
  }

  class SimpleSpreadsheetProcessor extends SpreadsheetProcessor {
    override def process(spreadsheet: RawSpreadsheet): ProcessedSpreadsheet = ???
  }

  trait SpreadsheetWriter {
    def write(writePath: String, processedSpreadsheet: ProcessedSpreadsheet): Either[ErrorMessage, RawSpreadsheet]
  }

  class LocalSpreadsheetWriter() extends SpreadsheetWriter {
    override def write(
      writePath:            String,
      processedSpreadsheet: ProcessedSpreadsheet
    ): Either[ErrorMessage, RawSpreadsheet] = ???
  }

  def main(args: Array[String]): Unit = {
    val readPath  = "get from args - command line args"
    val writePath = "get from args - command line args"

    for {
      validReadPath  <- validateReadPath(readPath)
      validWritePath <- validateWritePath(writePath)

      spreadsheetParser  = new LocalSpreadsheetParser(validReadPath)
      parsedSpreadsheet <- spreadsheetParser.parse()

      spreadsheetProcessor = new SimpleSpreadsheetProcessor
      processedSpreadsheet = spreadsheetProcessor.process(parsedSpreadsheet)

      spreadsheetWriter = new LocalSpreadsheetWriter
      _                <- spreadsheetWriter.write(validWritePath, processedSpreadsheet)
    } yield ()
  }
}
