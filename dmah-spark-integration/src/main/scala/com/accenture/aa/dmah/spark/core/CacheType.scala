package com.accenture.aa.dmah.spark.core


/**
 * This Enum contains all the storage level imformation.
 * Data persist in memory or in disk.
 * @author kanwar.singh
 *
 */
object CacheType extends Enumeration {
  
  type CacheType = Value
  val MEMORY,MEMORY_DISK,ONLY_DISK,MEMORY_SER = Value
}