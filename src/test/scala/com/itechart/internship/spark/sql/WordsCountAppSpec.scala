package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class WordsCountAppSpec extends AnyFreeSpec {

  "Words Count App Tests" - {
    "test run" in {
      WordsCountApp.mainTestable(spark)
    }
  }
}
