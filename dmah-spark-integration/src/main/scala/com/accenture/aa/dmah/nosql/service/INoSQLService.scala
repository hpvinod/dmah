package com.accenture.aa.dmah.nosql.service

import org.bson.Document
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject
import com.mongodb.casbah.MongoClient
import com.accenture.aa.dmah.nosql.core.NoSQLClient

/**
 * This interface is used to fetch or write data in or from mongo db
 * contains various function which interact with mongo APIs
 * @author kanwar.singh
 * */

trait INoSQLService extends Serializable{
  
  var nosqlClient: NoSQLClient
  
  /**
   * This method is used to insert data in to DB.
   
   * @param dbName
   * @param collection name
   * @param Dbobject list
   * @param indexlist
   * */
  
  def insertData(dbName:String, 
                 collectionName:String,
                 batchArgsList: java.util.List[DBObject],
                 indexList:List[String])
  
  /**
   * This method is used to get all the data from a collection.
   *
   * @param dbName
   * @param collection name
   * @return DBObject lists
   * */
  
  def getAllData(dbName:String, 
                 collectionName:String):java.util.List[DBObject] 
  
  /**
   * This method is used to delete a collection.
   *
   * @param dbName
   * @param collection name
   * */
  
  def dropCollection(dbName:String, 
                 collectionName:String)
  
  
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
                 collectionName:String,colName : String ,value:Any)
  
  
  /**
   * This method is used to get the no of rows in a collection.
   *
   * @param dbName
   * @param collection name
   * */
  
  def count(dbName:String, 
                 collectionName:String):Long
  
   
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
                 )
  
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
                 collectionName:String,obj : DBObject):java.util.List[DBObject]
  
  /**
   * This method is used to get the data from a collection as per the given aggregation schema. 
   * @param dbName
   * @param collection name
   * @param obj
   * 
   * */
  
   def aggregateData(dbName:String, 
                 collectionName:String,obj : DBObject)
  
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
                 )
 
}