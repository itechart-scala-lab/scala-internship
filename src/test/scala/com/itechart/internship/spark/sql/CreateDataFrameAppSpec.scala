package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class CreateDataFrameAppSpec extends AnyFreeSpec {

  "Create Spark DataFrame App Tests" - {
    "test run" in {
      CreateDataFrameApp.mainTestable(spark)
    }
  }
}
