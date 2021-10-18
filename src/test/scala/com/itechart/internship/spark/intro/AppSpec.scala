package com.itechart.internship.spark.intro

import com.itechart.internship.spark.intro.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class AppSpec extends AnyFreeSpec {

  "Spark App Tests" - {
    "test run" in {
      App.mainTestable(spark, "src/test/resources/data/SPARK_README.md")
    }
  }
}
