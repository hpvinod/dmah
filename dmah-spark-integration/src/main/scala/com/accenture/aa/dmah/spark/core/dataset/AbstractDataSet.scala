package com.accenture.aa.dmah.spark.core.dataset

import java.io.Serializable
import com.accenture.aa.dmah.spark.core.CacheType


abstract class AbstractDataSet extends Serializable {
  
  def cacheDataSet(cacheType: CacheType.Value): Unit

  def deleteDataSet(): Unit
  
}