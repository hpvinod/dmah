package com.accenture.aa.dmah.nosql.writer

import com.mongodb.DBObject
import com.accenture.aa.dmah.nosql.service.INoSQLService
import java.io.Serializable

/**
 * This interface is used to write data from any data set spark operation
 * @author kanwar.singh
 * 
 * */

trait IBatchContainer extends Serializable {
   
  var nosqlService: INoSQLService
   
  /**
   * This function is used to write data in to collections
   * 
   * @param arguments
   * @param dbName
   * @param collectionName
   * @param indexList
   * */
   
   def writeData(arguments:java.util.List[DBObject],
                 dbName:String,
                 collectionName:String, 
                 indexList:List[String])
  
   /**
   * This function is used to write data in to collections
   * that are left from write data
   * 
   * @param dbName
   * @param collectionName
   * */  
   
   def flushData (dbName:String,collectionName:String)
}
