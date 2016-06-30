package com.accenture.aa.dmah.spark.core

import com.accenture.aa.dmah.nosql.core.NoSQLInitializationInSpark
import scala.beans.BeanProperty
import java.io.Serializable
import java.util.ArrayList
import com.mongodb.DBObject
import com.mongodb.BasicDBObject
import com.accenture.aa.dmah.nosql.writer.IBatchWriter
import com.accenture.aa.dmah.nosql.writer.BatchContainerImpl
import com.accenture.aa.dmah.nosql.writer.IBatchContainer

class TestingClass extends Serializable{
  
  @BeanProperty
  var sparkContainer : SparkContainer = _
  
  def executeInsert(){
     val tupleList = Array("National_Tv,20",
                           "DMDR_Tv,10",
                           "Email_TV2,1200",
                           "Email_TV3,140",
                           "Email_TV1,90",
                           "Newspaper_TV,120",
                           "Newspaper_TV2,10",
                           "Newspaper_TV3,14",
                           "Newspaper_TV4,20"/*,
                           "Newspaper_TV5,1"*/)
	   
     val rdd = sparkContainer.sparkDriver.sc.parallelize(tupleList)
	   
	  // sparkContainer.broadcastBatchWriter();
     
     val broadcastBatch =  sparkContainer.broadcastBatchWriter(sparkContainer.batchContainer)
     
     /*rdd.foreach { x => 
       
       val batchCon = broadcastBatch.value;
       val mongoClient = batchCon.nosqlService.nosqlClient.mongoClient;
        if(mongoClient == null){
           batchCon.nosqlService.nosqlClient.mongoClient = NoSQLInitializationInSpark.mongoClient
       }
       val array =  x.split(",")
       val dataList = new ArrayList[DBObject];
       val dbobj = new BasicDBObject()
       dbobj.append(array(0),array(1))
			 dataList.add(dbobj)
       batchCon.writeData(dataList, "Amap4", "facts", null);
     }*/
    // batchcontainer.flushData("Amap4", "facts")
     
      val lst = new ArrayList[ DBObject]
      for(i<-0 to 8){
        val dbobj = new BasicDBObject()
        val key = "geometry"+i
        val value = "sine@ _"+i
        dbobj.append(key, value)
        //lst.add(dbobj)
      }
      
       lst.add(new BasicDBObject().append("_id", 101).append("name","John").append("age", 7).append("result", "P"))
       lst.add(new BasicDBObject().append("_id", 102).append("name","Jack").append("age", 8).append("result", "P"))
       lst.add(new BasicDBObject().append("_id", 103).append("name","James").append("age", 8).append("result", "F"))
       lst.add(new BasicDBObject().append("_id", 104).append("name","Joshi").append("age", 8).append("result", "P"))
    
     
      val dbName = "student"
      val collectionName = "information"
     //batchcontainer.nosqlService.nosqlClient.mongoClient = NoSQLInitializationInSpark.mongoClient
     
      //1
      //method used to insert data without spark
    // batchcontainer.writeData(lst, dbName, collectionName, null)
      
      //2
      //method used to get Data all the data from collection
      //val result = batchcontainer.nosqlService.getAllData(dbName, collectionName)
      //println(result)
     
      //3method used to remove all data 
//      batchcontainer.nosqlService.dropCollection(dbName, collectionName)
    
      //4
      //method remove a single document
//      val colName = "activity_3"
//      val value = "NationalTv _3"
//      batchcontainer.nosqlService.removeData(dbName, collectionName, colName, value)
//      
      //5
      //used to calculate th etotal number of rows in collection
//      val count =  batchcontainer.nosqlService.count(dbName, collectionName)
//      println(count)
       
      //6
      //used to update any document
      //batchcontainer.nosqlService.updateData(dbName,collectionName,new BasicDBObject().append("activity_5", "NationalTv _5"),
        //                                     new BasicDBObject("activity_10", "NationalTv _5"))
      
      //7
      //used to find the all the document which contains age equal to 8 abd result equals to P
//      val obj = new BasicDBObject().append("result", "P").append("age", 8)
//      val result = batchcontainer.nosqlService.getData(dbName, collectionName, obj)
//      println(result)
      
      //grouping and then adding values
     // val obj  = new BasicDBObject().append("$group",new BasicDBObject("_id","$result"))
      //batchcontainer.nosqlService.aggregateData(dbName, collectionName,obj)
      
      /*MongoDBObject("$group" ->
      MongoDBObject("_id" -> "$tags",
                    "authors" -> MongoDBObject("$addToSet" -> "$author")
      )*/
      
      
      
  }
}