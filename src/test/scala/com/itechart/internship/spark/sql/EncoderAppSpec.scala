package com.itechart.internship.spark.sql

import com.itechart.internship.spark.sql.TestSparkApi.spark
import org.scalatest.freespec.AnyFreeSpec

class EncoderAppSpec extends AnyFreeSpec {

  "Encoder App Tests" - {
    "test run" in {
      EncoderApp.mainTestable(spark)
    }
  }
}
