package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class WordsCountSqlAppSpec extends AnyFreeSpec {

  "Words Count SQL App Tests" - {
    "test run" in {
      WordsCountSqlApp.mainTestable(spark)
    }
  }
}
