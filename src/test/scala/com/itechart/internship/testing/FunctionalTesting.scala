package com.itechart.internship.testing

import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.selenium.WebBrowser

// Functional testing is a type of software testing that
// validates the software system against the functional requirements/specifications.
// The purpose of Functional tests is to test each function
// of the software application, by providing appropriate input,
// verifying the output against the Functional requirements.

// Functional testing mainly involves black box testing and it is not concerned about
// the source code of the application. This testing checks User Interface, APIs, Database,
// Security, Client/Server communication and other functionality of the Application Under Test.
// The testing can be done either manually or using automation.

object FunctionalTesting

class ItechartSiteSpec extends AnyFunSuite with WebBrowser {

  implicit val driver: WebDriver = new HtmlUnitDriver

  test("itechart domain could be found using Google") {
    goTo("https://google.com")

    assert(pageTitle == "Google")
    textField("q").value = "itechart"
    submit()
    assert(pageSource contains "itechart.by")
  }

}
