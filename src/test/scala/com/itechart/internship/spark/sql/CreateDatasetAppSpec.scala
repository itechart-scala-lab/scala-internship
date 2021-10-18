package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class CreateDatasetAppSpec extends AnyFreeSpec {

  "Create Dataset App Tests" - {
    "test run" in {
      CreateDatasetApp.mainTestable(spark)
    }
  }
}
