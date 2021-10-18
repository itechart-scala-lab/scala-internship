package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class ParallelizedCollectionAppSpec extends AnyFreeSpec {

  "Parallelized Collection App Tests" - {

    "test run" in {
      ParallelizedCollectionApp.mainTestable(spark)
    }
  }
}
