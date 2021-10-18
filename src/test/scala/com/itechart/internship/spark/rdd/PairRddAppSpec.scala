package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class PairRddAppSpec extends AnyFreeSpec {

  "Pair RDD App Tests" - {

    "test run" in {
      PairRddApp.mainTestable(spark)
    }
  }
}
