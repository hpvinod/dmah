package com.accenture.aa.dmah.core

import org.apache.spark.SparkContext
import org.springframework.context.ApplicationContext
import org.apache.spark
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.DataFrame
import java.util.Properties
import org.apache.hadoop.fs.FileSystem

/**
 * This class holds the global context used in application overall.
 *
 * @author payal.patel
 */
object GlobalContext {

  var applicationContext: ApplicationContext = _

  /* var sparkContext : SparkContext = _
 */
  /* var sqlContext : SQLContext = _   */

  var userJourney: DataFrame = _

  var salesData: DataFrame = _

  var investment: DataFrame = _
  
  var featureColumns : Array[String] = _
  
  var dataFrameTuple : Tuple2[DataFrame, DataFrame] = _
  
  var mappingProperties:Map[String,Properties] =_
  
  var hdfs : FileSystem = _
  
  var hdfsURI : String = _
}