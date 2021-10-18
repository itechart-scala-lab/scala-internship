package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class SqlAppSpec extends AnyFreeSpec {

  "SQL App Tests" - {
    "test run" in {
      SqlApp.mainTestable(spark)
    }
  }
}
