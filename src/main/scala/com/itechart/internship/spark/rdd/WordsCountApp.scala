package com.itechart.internship.spark.rdd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}

import java.net.URI

object WordsCountApp {

  def main(args: Array[String]): Unit = {
    val spark = new SparkContext(
      new SparkConf()
        .setAppName("Words Count Application")
    )

    mainTestable(spark)

    spark.stop()
  }

  def mainTestable(spark: SparkContext): Unit = {
    val words = spark
      .textFile("src/main/resources/data/lines.txt")
      .flatMap(line => line.split(" ")) // separate text into words
      .map(word => (word, 1)) // pair each word with 1
      .reduceByKey(_ + _) // sum up the 1s in the pairs

    // RDD does not give us the opportunity to overwrite
    val location = "output"
    val fs       = FileSystem.get(URI.create(location), new Configuration())
    fs.delete(new Path(location), true)
    words.saveAsTextFile(location)
  }

}
