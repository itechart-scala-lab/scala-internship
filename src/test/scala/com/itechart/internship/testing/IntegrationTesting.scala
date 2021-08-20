package com.itechart.internship.testing

import com.itechart.internship.testing.UserService._
import org.scalatest.funsuite.AnyFunSuite

import java.sql.{Connection, DriverManager}

// This is the most expensive type of the tests, because of potential flakiness
// and the need of the heavyweight machines to execute them.
//
// Nowadays, with advent of Selenium DSL etc., it is quite easy to use integration
// testing DSLs for the applications, and, as consequence, some teams are crazy
// overdoing with these. We recommend to push as much as possible testing towards
// unit and functional level tests.
//
// Integration tests could be run using embedded servers or using real virtual
// environments using docker containers etc.
//
// -
//
// Sometimes integration tests could be a necessity or, at least very useful.
// For example, if you need to check your SQL queries are working.
//
// Implement the missing tests for `UserService`.
//
// sbt:scala-bootcamp> testOnly *testing.UserServiceSpec
//
// Hint: do not use plain JDBC in Scala, there are much more convenient libraries
// for real work, i.e. Doobie, Quill, Slick etc. which provide automatic resource
// management, class mapping etc. You will learn them later during this course.
//
class UserService(connection: Connection) {

  def createTable(): Unit = {
    val statement = connection.createStatement()
    try {
      statement.execute("create table players(id VARCHAR, name VARCHAR, score INT)")
    } finally {
      statement.close()
    }
  }

  def insert(player: Player): Unit = {
    val statement = connection.createStatement()
    try {
      statement.execute(s"insert into players values ('${player.id}', '${player.name}', ${player.score})")
    } finally {
      statement.close()
    }
  }

}

object UserService {
  case class Player(id: String, name: String, score: Int)
}

class UserServiceSpec extends AnyFunSuite {

  class Fixture {
    Class.forName("org.h2.Driver")
    val connection = DriverManager.getConnection("jdbc:h2:mem:UserServiceSpec")
  }

  test("that we can create a table in a database") {
    val f       = new Fixture
    val service = new UserService(f.connection)
    service.createTable()
  }

  test("that we can insert a player") {
    val f       = new Fixture
    val service = new UserService(f.connection)
    service.insert(Player("id-001", "Max", 5))
  }

  // Question. What should be improved in those integration tests?

}
