package com.accenture.aa.dmah.spark.core

import org.apache.spark.sql.DataFrame
import java.io.Serializable

/**
 * This trait is used to query data from hive context
 * @author kanwar.singh
 *
 */
trait IQueryExecutor extends Serializable {
  def executeQuery(str:String):DataFrame
}