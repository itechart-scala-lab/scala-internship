package com.itechart.internship.spark.sql

import org.apache.spark.sql.SparkSession

object TestSparkApi {

  lazy val spark: SparkSession = {
    SparkSession
      .builder()
      .appName("Simple Application")
      .master("local[2]")
      //.config("spark.eventLog.enabled", "true")
      //.config("spark.eventLog.dir", "tmp/logs")
      .getOrCreate()
  }

}
