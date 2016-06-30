package com.accenture.aa.dmah.core.loaders

 
import java.io.File
import scala.beans.BeanProperty
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.core.exceptions.InitializationException
import com.accenture.aa.dmah.spark.core.SparkDriver
import com.accenture.aa.dmah.nosql.core.NoSQLClient

/**
 * This class will trigger the NoSQLClient class to initialize mongoDB instance
 * @author kanwar.singh
 */
class NoSqlLoader extends ILoader{
  
  val logger = LoggerFactory.getLogger(classOf[NoSqlLoader])
   
  @BeanProperty
  var nosqlClient: NoSQLClient = _
 

  @throws(classOf[InitializationException])
  def execute() = {
      nosqlClient.initialize
  }
}

