package com.itechart.internship.http

import cats.effect.IO
import cats.syntax.option._
import com.itechart.internship.http.HttpServer.httpApp
import org.http4s._
import org.http4s.implicits._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class HttpSpec extends AnyFreeSpec with Matchers {

  "HttpServer should" - {

    "validate hello world" in {
      val validResponseIO = httpApp.run(
        Request(
          method = Method.GET,
          uri    = uri"/hello/world"
        )
      )
      check[String](
        actualResponseIO = validResponseIO,
        expectedStatus   = Status.Ok,
        expectedBody     = "Hello, world!".some,
      )
    }
  }

  private def check[A](
    actualResponseIO:       IO[Response[IO]],
    expectedStatus:         Status,
    expectedBody:           Option[A],
    expectedResponseCookie: Option[ResponseCookie] = None,
  )(
    implicit
    decoder: EntityDecoder[IO, A],
  ): Unit = (for {
    actualResponse <- actualResponseIO
    _              <- IO(actualResponse.status shouldBe expectedStatus)
    _ <- expectedBody match {
      case None       => actualResponse.body.compile.toVector.map(_ shouldBe empty)
      case Some(body) => actualResponse.as[A].map(_ shouldBe body)
    }
    _ <- expectedResponseCookie match {
      case None                 => IO.unit
      case Some(responseCookie) => IO(actualResponse.cookies should contain(responseCookie))
    }
  } yield ()).unsafeRunSync()
}
