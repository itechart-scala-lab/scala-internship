package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class RddOperationsAppSpec extends AnyFreeSpec {

  "RDD Operations App Tests" - {

    "test run" in {
      RddOperationsApp.mainTestable(spark)
    }
  }
}
