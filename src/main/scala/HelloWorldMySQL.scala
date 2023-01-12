package com.github.sammyne.helloworld

import java.util.Properties

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.{functions => fn}

object HelloWorldMySQL extends App {

  val (tableUri, user, password) = this.args.toList match {
    case tableUri :: Nil => (tableUri, "root", "helloworld")
    case tableUri :: user :: password :: Nil =>
      (tableUri, user, password)
    case _ =>
      throw new IllegalArgumentException(
        s"miss tableUri, user, password",
      )
  }

  val (dbUri, tableName) = this.parseDbUriAndTableName(tableUri)
  println(s"dbUri = $dbUri, tableName = $tableName")

  val spark: SparkSession = SparkSession.builder
    .appName("mysql-table-courier")
    .master("local[*]")
    .getOrCreate

  // 打印配置信息用于调试
  // println(s"conf goes as\n${spark.sparkContext.getConf.toDebugString}")

  // Using properties
  val props = new Properties
  props.put("user", user)
  props.put("password", password)
  // props.put("useSSL", "false")
  props.put("numPartitions", "5")
  //props.put("partitionColumn", "id")
  //props.put("lowerBound", "0")
  //props.put("upperBound", "1000")

  val mysqlURL = s"jdbc:mysql://$dbUri"

  var inDF = spark.read.jdbc(mysqlURL, tableName, props)

  // Displays the dataframe and some of its metadata
  println(
    s"The dataframe contains ${inDF.count} record(s) with ${inDF.rdd.getNumPartitions} partition(s).",
  )

  inDF
    .withColumn("partitionId", fn.spark_partition_id())
    .groupBy("partitionId")
    .count()
    .show()

  val outDF = inDF.withColumn("value", fn.col("value") + 123)
  val outTableName = s"${tableName}_copy"

  // 构造结果输出地址
  println(s"save output to $dbUri in table ${outTableName}")

  outDF.write.mode(SaveMode.Overwrite).jdbc(mysqlURL, outTableName, props)

  println("done :)")

  def parseDbUriAndTableName(tableUri: String): (String, String) = {
    val idx = tableUri.lastIndexOf('/')
    if (idx == -1) {
      throw new IllegalArgumentException(
        s"miss tableName for tableUri $tableUri",
      )
    }
    val (dbUri, tableName) = tableUri.splitAt(idx)

    (dbUri, tableName.substring(1))
  }

  def parseIntOrThrow(s: String, hint: String): Int = {
    try { s.toInt }
    catch {
      case e: Throwable => throw new IllegalArgumentException(s"$hint: $e")
    }
  }
}
