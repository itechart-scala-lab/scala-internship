package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class AccumulatorVarAppSpec extends AnyFreeSpec {

  "Accumulator Variable App Tests" - {

    "test run" in {
      AccumulatorVarApp.mainTestable(spark)
    }
  }
}
