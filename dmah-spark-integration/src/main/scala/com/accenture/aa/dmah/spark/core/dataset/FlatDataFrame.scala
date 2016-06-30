package com.accenture.aa.dmah.spark.core

import org.apache.spark.sql.DataFrame


import scala.collection.JavaConversions._

import com.accenture.aa.dmah.spark.core.dataset.AbstractDataSet;

class FlatDataFrame[T](dataset: DataFrame) extends AbstractDataSet{
  
  var isCached: Boolean = _

  var operation = new Operations;
  
  var actions = new Actions;
  
  override def cacheDataSet(cacheType: CacheType.Value) {
    val storageLevel = SparkUtil.getStorageLevel(cacheType)
    val flatDataset = this.dataset
    flatDataset.persist(storageLevel)
  }
  
  override def deleteDataSet() {
    this.dataset.unpersist(true)
  }
  
  class Operations {
   def select () = {
     
   }
   def groupBy() = {
     
   }
   def col(colName:String) = {
     /*val flatDataFrame  = new FlatDataFrame(dataset.col(colName));
     flatDataFrame*/
   }
  }
  
  class Actions {
      def collect(): Any  = {
       dataset.collect() 
      }
      def show() = {
        dataset.show()
      }
   }
}  
