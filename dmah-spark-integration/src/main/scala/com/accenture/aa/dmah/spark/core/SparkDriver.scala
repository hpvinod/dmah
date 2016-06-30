package com.accenture.aa.dmah.spark.core

import java.util.Map
import scala.beans.BeanProperty
import scala.collection.JavaConversions._
import org.apache.spark.SparkConf
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.core.exceptions.InitializationException
import org.apache.spark.sql.SQLContext
import com.accenture.aa.dmah.core.GlobalContext
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import com.accenture.aa.dmah.nosql.core.NoSQLInitializationInSpark
import java.io.Serializable
import com.accenture.aa.dmah.nosql.core.NoSQLClient
import com.accenture.aa.dmah.nosql.core.NoSqlInitialization
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}



/**
 * This class responsible to initialize spark 
 * 
 */
class SparkDriver extends Serializable{

  val logger = LoggerFactory.getLogger(classOf[SparkDriver])
 
  @BeanProperty
  var sparkConfig: Map[String, String] = _
  
  @BeanProperty
  var systemSparkConfig: Map[String, String] = _
  
  @BeanProperty
  var driver: SparkDriver = null
	
  @BeanProperty
	var appName:String = _
	
	@BeanProperty
  var nosqlClient: NoSQLClient = _
  
  @BeanProperty
  var nosqlInitialization: NoSqlInitialization = _
  
  @BeanProperty
   var hdfsURI : String = _
 
  @transient
	var sc: SparkContext = _
	
	@transient
	var sqlContext : SQLContext = _
	
	@transient
	var hadoopConf : Configuration = _
	
 def initializeSparkContext() {
	  
    logger.info("Initialize sparkContext")
   
    try{
      
      val conf = new SparkConf().setAppName(appName)
      
      /**
       * populate spark configuration from xml
       */
      for ((key, value) <- getSparkConfig) {
        conf.set(key, value)
      }

      for ((key, value) <- getSystemSparkConfig()) {
        System.setProperty(key, value)
      }
      if(null == sc && null == sqlContext)
      {
        sc = new SparkContext(conf)
        sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
      }
      
      nosqlInitialization.initialize(sc, nosqlClient);
      
      /*Initializing the connection to  hadoop filesystem*/
      
      hadoopConf = new Configuration()
      hadoopConf.set("fs.defaultFS", hdfsURI)
      GlobalContext.hdfsURI = hdfsURI
      val hdfs= FileSystem.get(hadoopConf)
      GlobalContext.hdfs = hdfs
      
      logger.info("********************************sparkContext initialized********************************")
      
    }catch{
       case ex: InitializationException => {
            logger.error("Error getting while initilize spark context",ex)
            throw ex;
        }
    }
  }
  
  /**
   * This method is used to initialize no sql connections on every node in cluster
   * 
   * */
  
  def initializeNoSQL(){} 
}

