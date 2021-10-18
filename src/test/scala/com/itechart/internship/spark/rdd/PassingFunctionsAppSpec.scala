package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class PassingFunctionsAppSpec extends AnyFreeSpec {

  "Passing Functions App Tests" - {

    "test run" in {
      PassingFunctionsApp.mainTestable(spark)
    }
  }
}
