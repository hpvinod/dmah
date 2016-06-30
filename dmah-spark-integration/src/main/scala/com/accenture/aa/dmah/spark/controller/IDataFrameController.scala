package com.accenture.aa.dmah.spark.controller

import com.accenture.aa.dmah.spark.core.CacheType
import org.apache.spark.sql.DataFrame

/**
 * This trait provides the persistance api's to Data frame
 * @author kanwar.singh
 *
 */
trait IDataFrameController {
  
  def persistDataSet(dataset:DataFrame,cacheType: CacheType.Value):Unit

  def deleteDataSet(dataset:DataFrame):Unit
  
}