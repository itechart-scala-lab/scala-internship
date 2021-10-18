package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class RddPersistAppSpec extends AnyFreeSpec {

  "Rdd Persist App Tests" - {

    "test run" in {
      RddPersistApp.mainTestable(spark)
    }
  }
}
