package com.itechart.internship.spark.rdd

import com.itechart.internship.spark.rdd.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class WordsCountAppSpec extends AnyFreeSpec {

  "Words Count App Tests" - {

    "test run" in {
      WordsCountApp.mainTestable(spark)
    }
  }
}
