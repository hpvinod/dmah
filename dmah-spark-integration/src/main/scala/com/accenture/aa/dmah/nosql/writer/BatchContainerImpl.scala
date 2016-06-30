package com.accenture.aa.dmah.nosql.writer

import com.accenture.aa.dmah.nosql.service.INoSQLService
import scala.beans.BeanProperty

import scala.collection.mutable.SynchronizedMap
import scala.collection.immutable.HashMap
import java.util.Collections.SynchronizedMap
import java.util.Collections.SynchronizedMap
import java.util.Collection
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import com.mongodb.DBObject
import java.io.Serializable

/**
 * This class is used to write data from any data set spark operation
 * It also holds the mapping of BatchWriter objects as per collection name and db name
 * 
 * @author kanwar.singh
 * 
 * */

class BatchContainerImpl extends IBatchContainer {
  
  var batchWriterMap:ConcurrentHashMap[String,ConcurrentHashMap[String,BatchWriter]] = _
  
  @BeanProperty
  var nosqlService: INoSQLService = _
 
  @BeanProperty
  var batchcount:String =_
  
  /**
   * This function is used to create instances of batch writer per collection and per Db
   * if not created 
   * 
   * @param dbname
   * @param collectionName
   * 
   * */
  
  def getBatchWriter(dbName : String,collectionName : String){  
      if(batchWriterMap != null){
        if(batchWriterMap.get(dbName) != null){
          if(batchWriterMap.get(dbName).get(collectionName) == null){
            batchWriterMap.get(dbName).put(collectionName, new BatchWriter(batchcount,nosqlService))
          }
        }
        else{
             val collectionMap = new ConcurrentHashMap[String,BatchWriter]
             collectionMap.put(collectionName, new BatchWriter(batchcount,nosqlService))
             batchWriterMap.put(dbName, collectionMap)
        }
      }
      else{
        batchWriterMap = new ConcurrentHashMap
        val collectionMap = new ConcurrentHashMap[String,BatchWriter]
        collectionMap.put(collectionName, new BatchWriter(batchcount,nosqlService))
        batchWriterMap.put(dbName, collectionMap)
        
      }
    }
  
  /**
   * This function is used to write data in to collections
   * 
   * @param arguments
   * @param dbName
   * @param collectionName
   * @param indexList
   * */
  
   def writeData (arguments:java.util.List[DBObject],
                   dbName:String,collectionName:String, 
                   indexList:List[String]){
      this.getBatchWriter(dbName, collectionName)
      batchWriterMap.get(dbName).get(collectionName).writeData(arguments, dbName, collectionName, indexList)
   }
  
  /**
   * This function is used to write data in to collections
   * that are left from write data
   * 
   * @param dbName
   * @param collectionName
   * */
   
    def flushData (dbName:String,collectionName:String){
      this.getBatchWriter(dbName, collectionName)
      batchWriterMap.get(dbName).get(collectionName).flushData(dbName, collectionName)
    }
    
  
  
  
  
  
}