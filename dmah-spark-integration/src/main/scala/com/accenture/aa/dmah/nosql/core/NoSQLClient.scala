package com.accenture.aa.dmah.nosql.core

import scala.beans.BeanProperty
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory
import com.mongodb.casbah.MongoClient
import com.accenture.aa.dmah.nosql.service.INoSQLService
import java.io.Serializable


/**
 * This class responsible to initialize MongoDB 
 * @author kanwar.singh
 * 
 */
class NoSQLClient extends Serializable{

  val logger = LoggerFactory.getLogger(classOf[NoSQLClient])
 
  @BeanProperty
  var serverIP: String = _
 
  @BeanProperty
  var port: String = _
 
  @transient
  var mongoClient: MongoClient = _

	/**
	 * This method is used to initialize noSql client
	 * 
	 * */
	
  def initialize =  {
    //mongoClient = MongoClient(serverIP,port.toInt)
    mongoClient = NoSQLInitializationInSpark.mongoClient
  }
  
  
    
}

