package com.itechart.internship.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

object TestSparkApi {

  lazy val spark: SparkContext = {
    new SparkContext(
      new SparkConf()
        .setAppName("Simple Application")
        .setMaster("local[2]")
      //.set("spark.eventLog.enabled", "true")
      //.set("spark.eventLog.dir", "../tmp/logs")
    )
  }

}
