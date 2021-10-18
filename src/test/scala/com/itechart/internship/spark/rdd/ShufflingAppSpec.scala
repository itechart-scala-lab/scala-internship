package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class ShufflingAppSpec extends AnyFreeSpec {

  "Pair RDD App Tests" - {

    "test run" in {
      ShufflingApp.mainTestable(spark)
    }

    "test improved run" in {
      ShufflingApp.mainTestableImproved(spark)
    }
  }
}
