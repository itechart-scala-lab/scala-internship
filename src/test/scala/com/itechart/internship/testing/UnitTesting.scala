package com.itechart.internship.testing

import org.scalatest.EitherValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.annotation.nowarn

// *Introduction*
//
// If we cannot avoid doing the feature and we cannot make sure compiler or
// static checker tests for the feature to work correctly, we have to rely
// on unit testing. Unit testing is extremely powerful for Scala these days.
//
// The good developer will sense where unit tests are needed and will write
// them himself, but a lot of guys prefer to use coverage measuring tools to
// spot the parts which are not covered. The typical tool Scala guys would
// use for it is http://scoverage.org/. Note, that we do not strive for
// 100% coverage anymore these days, because of lower levels serving us,
// and other reasons, but it is a really cool way to discover the blind
// spots.
//
// The main issue here is to not overdo. If we see that some part is not
// covered by unit tests, we might first ask ourselves if lower layer would
// be a better place to check the property, or if it is cost-effective to test
// it. I.e. testing and maintaining the tests should be lower than cost of
// the bug multiplied by probability of it to happen. Otherwise it makes no
// sense to test for it.
//
// Basically we should have hundreds or thousands of unit tests per every app
// (depends on the size of app). The most typical usage is testing business
// logic of the app, but there are a lot more various use
// cases.
//
// * Structure *
//
// The most popular build tool in Scala - sbt.
// You just put your code into `src/main/scala` and you tests into
// `src/test/scala` and it compiles and runs everything automatically
// without a configuration.
//
// The classes under test for this workshop are stored in `src/main/scala/testing`
//
// Your IDE - IntelliJ also understands this
// convention and already knows how to run the tests using popular testing
// frameworks directly from IDE. For some advanced cases you might want to use
// sbt directly though.
//
// Let's try to run all the tests for the project using `sbt`. Run `sbt test`
// inside of `scala-internship` directory.
//
// > sbt test
//
// Some tests fails, some test pass, but it takes quite a time to start them.
// It takes at least several seconds on machine.
//
// Why? This is because `sbt` is JVM application and takes considerable time
// to load. How much the tests took in total on your computer?
//
// There is also a command which only runs the tests failing a previous
// run or the tests which have their dependencies changed. Try it out.
// Did you have less tests running this time?
//
// sbt testQuick
//
// -
//
// There are several popular testing libraries for Scala in existence. Arguably,
// the most popular and one of most flexible is called `ScalaTest`. One of the
// reasons why it is so popular is because it supports a lot of testing styles
// and DSLs.
//
// So, what are the styles ScalaTest support? You can find them on the following
// page: https://www.scalatest.org/user_guide/selecting_a_style
//
// One of the most popular is `FreeSpec` which allows one to write your test cases in a free form.
//
// Run the following suite using the command below:
//
// sbt:scala-bootcamp> testOnly *testing.CalculatorSpec
//
// Now break one of the tests, i.e. change `calculator.enter(1)` to
// `calculator.enter(2)`. Observe the output. How did Scala manage
// to output such a thing?
class CalculatorSpec extends AnyFreeSpec {

  "calculator" - {
    "enters the number correctly" in {
      val calculator = Calculator()
      assert(calculator.enter(1) == Right(Calculator(1, 0, None)))
      assert(calculator.enter(7) == Right(Calculator(7, 0, None)))
      assert(calculator.enter(12) == Left("digit out of range"))
    }
    "does nothing" - {
      "when you just repeat pressing `=`" in {
        val calculator = Calculator()
        assert(calculator.calculate.calculate.calculate.calculate == calculator)
      }
    }
  }
}

// *Note*
//
// Which style do you like more? Are you ready to argue with your colleagues
// for several days over the best style? Scala developers used to fight about
// it a lot in early days. Not anymore though...

// What does `assert` word actually do? Can you write it differently?
//
// Both `ScalaTest` and `Specs2` support writing so called matchers which
// make it easier to write human readable tests.
//
// The detailed documentation could be found here:
// https://www.scalatest.org/user_guide/using_matchers
//
// Rewrite asserts to `should be` matcher and run it using sbt again:
//
// sbt testOnly *testing.CalculatorWithMatchersFreeSpec
//
// Now break one of the tests, i.e. change `calculator.enter(1)` to
// `calculator.enter(2)`. Observe the output. Do you like the input?
// How does it compare to what you seen in `Exercise1`?
class CalculatorWithMatchersFreeSpec extends AnyFreeSpec with Matchers {

  "calculator" - {
    "enters the number correctly" in {
      val calculator = Calculator()
      calculator.enter(1) should be(Right(Calculator(1, 0, None)))
      assert(calculator.enter(7) == Right(Calculator(7, 0, None)))
      assert(calculator.enter(12) == Left("digit out of range"))
    }
    "does nothing" - {
      "when you just repeat pressing `=`" in {
        val calculator = Calculator()
        assert(calculator.calculate.calculate.calculate.calculate == calculator)
      }
    }
  }
}

//
// This test, arguably, now looks a bit more readable. Can we get rid
// of these verbose `Right` and `Left` words?
//
// Actually we can and there is whole construct for that in ScalaTest:
// https://www.scalatest.org/user_guide/using_EitherValues
//
// There is also similar construct for `Option`, `PartialFunction` etc.
//
// Let's rewrite asserts from the first Exercise (or previous Exercise
// if you prefer so) to the new way. There is one line already rewritten
// so you can have an example.
//
// sbt testOnly *testing.CalculatorWithEitherFreeSpec
//
// Now break one of the tests, i.e. change `calculator.enter(1)` to
// `calculator.enter(900)`. Observe the output. Do you like the input?
// How does it compare to what you seen in Exercise 2?
@nowarn
class CalculatorWithEitherFreeSpec extends AnyFreeSpec with EitherValues {

  "calculator" - {
    "enters the number correctly" in {
      val calculator = Calculator()
      assert(calculator.enter(1).right.value == Calculator(1, 0, None))
      assert(calculator.enter(7) == Right(Calculator(7, 0, None))) // Exercise
      assert(calculator.enter(12) == Left("digit out of range")) // Exercise
    }
    "does nothing" - {
      "when you just repeat pressing `=`" in {
        val calculator = Calculator()
        assert(calculator.calculate.calculate.calculate.calculate == calculator)
      }
    }
  }

}

//
// sbt testOnly *testing.CalculatorFunSpec
//
class CalculatorFunSpec extends AnyFunSuite {

  test("enters the number correctly") {
    val calculator = Calculator()
    assert(calculator.enter(1) == Right(Calculator(1, 0, None)))
    assert(calculator.enter(7) == Right(Calculator(7, 0, None)))
    assert(calculator.enter(12) == Left("digit out of range"))
  }

  test("does nothing when you just repeat pressing `=`") {
    val calculator = Calculator()
    assert(calculator.calculate.calculate.calculate.calculate == calculator)
  }
}
