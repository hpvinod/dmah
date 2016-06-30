package com.accenture.aa.dmah.nosql.core


import java.util.Arrays
import scala.collection.JavaConversions._
import com.mongodb.casbah.MongoClient

/**
 * This object initialize mongo client instance
 * @author kanwar.singh
 * 
 * */

object NoSQLInitializationInSpark {

  var mongoClient: MongoClient = _

  private var inInitializedOnce: java.lang.Boolean = false

  def initializeMongoClient(serverip :String,port:Integer) {
    if (!inInitializedOnce) {
        mongoClient = MongoClient(serverip,port)
        inInitializedOnce = true
      }
  }
}
