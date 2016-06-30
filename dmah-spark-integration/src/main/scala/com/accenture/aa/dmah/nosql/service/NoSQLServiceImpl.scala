package com.accenture.aa.dmah.nosql.service

import scala.beans.BeanProperty

import com.accenture.aa.dmah.nosql.core.NoSQLClient
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.casbah.WriteConcern
import com.mongodb.DBObjects
import com.mongodb.DBCursor
import com.mongodb.casbah.MongoClient
import java.io.Serializable
import com.accenture.aa.dmah.nosql.core.NoSQLInitializationInSpark
 
/**
 * This class is used to fetch or write data in or from mongo db
 * contains various function which interact with mongo APIs
 * @author kanwar.singh
 * */

class NoSQLServiceImpl extends INoSQLService {
  
  @BeanProperty
  var nosqlClient: NoSQLClient = _
 
  @BeanProperty
  var batchsize:String = _
  
   /**
   * This method is used to insert data in to DB.
   
   * @param dbName
   * @param collection name
   * @param Dbobject list
   * @param indexlist
   * */
  
  def insertData(dbName:String, 
                 collectionName:String, 
                 batchArgsList:java.util.List[DBObject],
                 indexList:List[String]){
    
    var dbCollection = getCollection(dbName, collectionName, indexList)
 	   dbCollection.insert(batchArgsList, WriteConcern.Acknowledged)
	}
  
  
  /**
   * This method is used to remove the data from a collection as per the given dbobjects. 
   * @param dbName
   * @param collection name
   * @param batchArgsList
   * 
   * */
  
  def deleteData(dbName:String, 
                 collectionName:String, 
                 batchArgsList:java.util.List[DBObject]
                 ){
    
    var dbCollection = getCollection(dbName, collectionName, null)
    for(i <- 0 to batchArgsList.size()){
        dbCollection.remove(batchArgsList.get(i))
    }
   
 	}
  
   /**
   * This method is used to get the no of rows in a collection.
   *
   * @param dbName
   * @param collection name
   * */
  
  def count(dbName:String, 
                 collectionName:String):Long = {
     var dbCollection = getCollection(dbName, collectionName, null)
     val count = dbCollection.count();
     count
  }
  
  /**
   * This method is used to delete a collection.
   *
   * @param dbName
   * @param collection name
   * */
  
  def dropCollection(dbName:String, 
                 collectionName:String){
     var dbCollection = getCollection(dbName, collectionName, null)
     dbCollection.drop()
  }
  
  /**
   * This method is used to remove a particular value from a table.
   *
   * @param dbName
   * @param collection name
   * @param colName
   * @param value
   * 
   * */
  
  def removeData(dbName:String, 
                 collectionName:String,colName : String ,value:Any){
    var dbCollection = getCollection(dbName, collectionName, null)
    dbCollection.remove(new BasicDBObject().append(colName, value))
  }
  
  /**
   * This method is used to update the data in to collection 
   * old obj is the object that will replace with the new object.
   *
   * @param dbName
   * @param collection name
   * @param oldObj
   * @param newObj
   * */
  
  def updateData(dbName:String, 
                 collectionName:String, 
                 oldObj:DBObject,
                 newObj:DBObject
                 ){
    
     var dbCollection = getCollection(dbName, collectionName, null)
     dbCollection.update(oldObj, newObj)
 	}
  
   /**
   * This method is used to get all the data from a collection.
   *
   * @param dbName
   * @param collection name
   * @return DBObject lists
   * */
  
  def getAllData(dbName:String, 
                 collectionName:String):java.util.List[DBObject] = {
       var dbCollection = getCollection(dbName, collectionName, null)
       val result = dbCollection.find()
       result.toArray()
  }  
  
  /**
   * This method is used to get the data from a collection. 
   * with where conditions specified in the given DB object.
   *
   * @param dbName
   * @param collection name
   * @param obj
   * 
   * */
  
  def getData(dbName:String, 
                 collectionName:String,obj : DBObject):java.util.List[DBObject] = {
     var dbCollection = getCollection(dbName, collectionName, null)
     var result = dbCollection.find(obj)
     result.toArray()
  }
  
  /**
   * This method is used to get the data from a collection as per the given aggregation schema. 
   * @param dbName
   * @param collection name
   * @param obj
   * 
   * */
  
   def aggregateData(dbName:String, 
                 collectionName:String,obj : DBObject)/*:java.util.List[DBObject]*/ = {
     var dbCollection = getCollection(dbName, collectionName, null)
     var result = dbCollection.aggregate(obj)
     println(result)
     //result
  }
  
   /**
   * Method used to create collection
   * 
   * @param dbtype weather collection created for intermediate store or final output
   * @param collectionName  
   * @param indexList - to create index for particular collection
   * 
   */
 private def getCollection(dbName:String, 
                           collectionName: String, 
                           indexList: List[String]):DBCollection  =   {
    // get handle to  database
    val database = NoSQLInitializationInSpark.mongoClient.getDB(dbName)

    // get a handle to the "test" collection
    val collection = database.getCollection(collectionName)
    
    if( indexList != null){
        val doc = new BasicDBObject
        indexList.foreach { x => doc.append(x,1) }
        collection.createIndex(doc)
    } 
    collection
  }
  
}