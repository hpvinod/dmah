package com.accenture.aa.dmah.core.loaders
import java.io.File
import scala.beans.BeanProperty
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.core.exceptions.InitializationException
import com.accenture.aa.dmah.spark.core.SparkDriver

/**
 * This class will trigger the sparkbootup class to initilze spark context
 */
class SparkLoader extends ILoader{
  
  val logger = LoggerFactory.getLogger(classOf[SparkLoader])
   
  @BeanProperty
  var sparkDriver: SparkDriver = _
 
  @throws(classOf[InitializationException])
  def execute() = {
    sparkDriver.initializeSparkContext()
  }
}

