package com.itechart.util.logging

object Logger {

  def info(str: String): Unit =
    println(s"info: $str")

  def warn(str: String): Unit =
    println(s"warn: $str")

  def error(str: String): Unit =
    println(s"error: $str")
}
