package com.accenture.aa.dmah.nosql.writer

import com.mongodb.DBObject
import com.accenture.aa.dmah.nosql.service.INoSQLService
import java.io.Serializable

/**
 * This trait is used for creating a batch of records as per the batch count
 * and then write the whole batch directly in to DB 
 
 * @author kanwar.singh
 * 
 * */
 

trait IBatchWriter extends Serializable{
  
  /**
    * This method create a batch of records that are coming from 
    * a spark job once the batch size is full it will dump all the data in to Db
    * 
    * @param arguments
    * @param dbname
    * @param collection name
    * @param indexList
    * 
    * 
    */
   
  def writeData(arguments:java.util.List[DBObject],
                 dbName:String,
                 collectionName:String, 
                 indexList:List[String])
  
  /**
    * This method create a remaining records that are coming 
    * that are left from write data 
    * 
    * @param dbname
    * @param collection name
    * */
  
  def flushData(dbName:String,collectionName:String)
}