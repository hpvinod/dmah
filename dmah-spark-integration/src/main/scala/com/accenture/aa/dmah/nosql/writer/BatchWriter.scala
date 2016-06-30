package com.accenture.aa.dmah.nosql.writer

 import com.accenture.aa.dmah.nosql.service.INoSQLService
import scala.beans.BeanProperty
import java.util.Collections
import java.util.ArrayList
import org.bson.Document
import com.mongodb.DBObject
import com.mongodb.DBObjects
import java.io.Serializable
import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * This class is used for creating a batch of records as per the batch count
 * and then write the whole batch directly in to DB 
 * contains different write method for writing data, one from spark job 
 * and one from spark driver
 * 
 * @author kanwar.singh
 * */
 
 

class BatchWriter(batchcount:String,nosqlService: INoSQLService) extends IBatchWriter{
  
  val logger = LoggerFactory.getLogger(classOf[BatchWriter])  
  
  var batchArgsList: java.util.List[DBObject] = Collections.synchronizedList(new ArrayList)
  
  /**
   * This method is used for writing data from spark driver to db 
   * creates thread as per the size of collection records and batch count
   * 
   * @param rowList
   * @param dbname
   * @param collection name 
   * @param indexList
   * */
  
  def writeData(rowList:java.util.List[DBObject],dbName:String,
      collectionName:String, indexList:List[String]){
    
     val rowCount = rowList.size
	   logger.debug("rowCount..." + rowCount)
	   var batchSize = batchcount.toInt;
	   val divisonFactor = rowCount / batchSize
		 logger.debug("divison.." + divisonFactor)
		 var remainder = 0
	   var upperlimit = rowCount
	   if (divisonFactor != 0) {
				   remainder = rowCount % batchSize
			   }
     else{
				   upperlimit = rowCount
				   remainder = rowCount % batchSize
		  }
	   val context = Executors.newCachedThreadPool()
	   if (divisonFactor != 0) {
	     for (index <- 0 until divisonFactor) {
	        val cnt = index
	        context.submit(new Runnable() {
	           override def run() {
	             process(cnt * batchSize, (cnt + 1) * batchSize - 1, rowList,
	                     dbName,collectionName,indexList);
	           }
	        });
	        }
	     }
	   if (remainder != 0){
	     process(divisonFactor * batchSize, upperlimit - 1, rowList,dbName:String,collectionName:String, indexList:List[String]);
	   }
	}
  
  /**This method is used to write records in to Db
   * 
   * @param lowerLimit
   * @param upperlimit
   * @param collection name
   * @param indexlist
   * 
   * */
  
  def process(lowerLimit:Int, upperLimit:Int,rows:java.util.List[DBObject],
               dbName:String,collectionName:String, indexList:List[String]){
     var limit = lowerLimit
     if (limit <= 0) {
			  limit = 1;
		  }
      val lst  = new ArrayList[DBObject]
     	for (i <- lowerLimit to upperLimit) {
     	   lst.add(rows.get(i));
     	}
      nosqlService.insertData(dbName,collectionName, lst,indexList)
   }
   
   /**
    * This method create a batch of records that are coming from 
    * a spark job once the batch size is full it will dump all the data in to Db
    * 
    * @param arguments
    * @param dbname
    * @param collection name
    * @param indexList
    * 
    * */
  
   def writeData(arguments:DBObject,dbName:String,collectionName:String, indexList:List[String]){
       batchArgsList.add(arguments);
    	if(batchArgsList.size() ==  batchcount.toInt){
    	   this.synchronized {
    	       var batchListClone = new ArrayList[DBObject](batchArgsList)
             batchArgsList.clear
             nosqlService.insertData(dbName,collectionName, batchListClone,indexList)
    	   }
			}
  }
   
  /**
    * This method create a remaining records that are coming 
    * that are left from write data 
    * 
    * @param dbname
    * @param collection name
    * */
   
  def flushData(dbName:String,collectionName:String){
      nosqlService.insertData(dbName,collectionName, batchArgsList,null)
      batchArgsList.clear
  }
  
}