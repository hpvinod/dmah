package com.accenture.aa.dmah.spark.core

import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext
import scala.beans.BeanProperty
import java.io.Serializable

/**
 * This class is used to query data from hive context
 * @author kanwar.singh
 *
 */

class QueryExecutor extends IQueryExecutor{
 
  @BeanProperty
	var sparkDriver : SparkDriver = _
	
	/**
	 * This mehtod is used to execute query over hive context
	 * */
	
	def executeQuery(query : String) =  {
    val dataSet = sparkDriver.sqlContext.sql(query)
    dataSet
  }
	
	
}