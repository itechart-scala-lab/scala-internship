package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class SparkSessionAppSpec extends AnyFreeSpec {

  "Spark Session App Tests" - {
    "test run" in {
      SparkSessionApp.mainTestable(spark)
    }
  }
}
