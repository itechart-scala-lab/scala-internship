package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class BroadcastVarAppSpec extends AnyFreeSpec {

  "Broadcast Variable App Tests" - {

    "test run" in {
      BroadcastVarApp.mainTestable(spark)
    }
  }
}
