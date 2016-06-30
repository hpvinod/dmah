package com.accenture.aa.dmah.spark.controller

import com.accenture.aa.dmah.spark.core.SparkUtil
import com.accenture.aa.dmah.spark.core.CacheType
import org.apache.spark.sql.DataFrame

/**
 * This class provides the life cycle api's to Data frame
 * @author kanwar.singh
 *
 */

class DataFrameController extends IDataFrameController {
  
  /**
    * This method is used to persist dataframe depedending
    * upon the cache type
   */
  
  def persistDataSet(dataset:DataFrame,cacheType: CacheType.Value) = {
    val storageLevel = SparkUtil.getStorageLevel(cacheType)
    dataset.persist(storageLevel)
  }
  
  /**
    * This method is used to unpersist dataframe
   */
  
  def deleteDataSet(dataset:DataFrame) =  {
    dataset.unpersist(true)
  }
  
}