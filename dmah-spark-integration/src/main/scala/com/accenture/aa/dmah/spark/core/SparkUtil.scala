package com.accenture.aa.dmah.spark.core

import org.apache.spark.storage.StorageLevel

object SparkUtil {
   
  def getStorageLevel(cacheType: CacheType.Value): StorageLevel = {
    var storageLevel: StorageLevel = null
    cacheType match {
      case CacheType.MEMORY => storageLevel = StorageLevel.MEMORY_ONLY
      case CacheType.MEMORY_DISK => storageLevel = StorageLevel.MEMORY_AND_DISK
      case CacheType.ONLY_DISK => storageLevel = StorageLevel.DISK_ONLY
      case CacheType.MEMORY_SER => storageLevel = StorageLevel.MEMORY_ONLY_SER
      case _ => storageLevel = StorageLevel.MEMORY_ONLY
    }
    storageLevel
  }
}