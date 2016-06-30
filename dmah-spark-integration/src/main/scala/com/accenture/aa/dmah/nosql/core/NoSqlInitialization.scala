package com.accenture.aa.dmah.nosql.core

import org.apache.spark.SparkContext
import java.io.Serializable

/**
 * This class is used to initialize no sql connections in spark
 * */

class NoSqlInitialization extends Serializable {
  
  /**
   * This method is used to initialize no sql connections on every node in cluster
   * 
   * */
  def initialize(sc:SparkContext,nosqlClient:NoSQLClient) = {
    val tupleList = Array("106","109","13")
		val rdd = sc.parallelize(tupleList)
		val serverIp = sc.broadcast(nosqlClient.serverIP)
		val port = sc.broadcast(nosqlClient.port);
		rdd.foreach { x => 
		       NoSQLInitializationInSpark.initializeMongoClient(serverIp.value,port.value.toInt)
     }
  
  }
}