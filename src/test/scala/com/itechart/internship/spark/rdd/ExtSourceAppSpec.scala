package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class ExtSourceAppSpec extends AnyFreeSpec {

  "External Source App Tests" - {

    "test run" in {
      ExtSourceApp.mainTestable(spark)
    }
  }
}
