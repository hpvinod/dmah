package com.accenture.aa.dmah.core

import org.springframework.context.support.ClassPathXmlApplicationContext
import com.accenture.aa.dmah.core.exceptions.InitializationException
import org.apache.log4j.xml.DOMConfigurator
import org.slf4j.LoggerFactory
import scala.io.Source
import java.util.Properties
import com.accenture.aa.dmah.core.util.ApplicationUtil
import org.springframework.beans.factory.NoSuchBeanDefinitionException

 
/**
 * A Container that is used to start the application at startup.
 * This will load application context
 * 
 * @author kanwar.singh
 *
 */
class Bootstrap(input:String) {
  
  private val Log4jConfigParam = "log4j.xml"
  val logger = LoggerFactory.getLogger(classOf[Bootstrap])  
  var applicationInstance:ApplicationInstance = new ApplicationInstance()
 
  var job:String = _
  var userInput:String = input
  var propertiesFileName:String=_
  
  def isValidInput() : Boolean={
    try{
           
          job = userInput;
    
          /**
           * if user will provide valid input jobtype then will return true,else wil return false
           */
          if(null != ApplicationUtil.retrieveBean(job)){
            propertiesFileName = ApplicationUtil.retrieveBean(job).asInstanceOf[IJobRunner].propertiesFileName;
            true
          } else false
         
      }catch{
        
          case ex: NoSuchBeanDefinitionException => {
            logger.error("Error getting while application bootup" + ex)
            return false
          }
      }
  }
  /**
   * This method is used to initalize application context*/
  def initContext() = {
    try{
    	  /**
    	   * loading application context*/
        val context = new ClassPathXmlApplicationContext("resources/conf/applicationContext.xml")
        GlobalContext.applicationContext = context
    }catch{
        case ex: InitializationException => {
            logger.error("Error getting while application bootup" + ex)
        }
        
      }
  }
 
	/**
    *
    * This method  used to load log4j.xml
   */
	def initLogging() = {
      
      DOMConfigurator.configure(Log4jConfigParam)
      logger.info("logger initialized")
    }
 
	/**
   * This method initializes the properties files depending upon the job type
   * */
	
   def initProperties(input:String) = {
      val filePath = "/resources/"+ propertiesFileName
      val source =  Source.fromURL(getClass.getResource(filePath))
      val properties = new Properties()
      properties.load(source.bufferedReader())
      val map = Map(input -> properties);
      GlobalContext.mappingProperties = map
   }
   
   def boot(){
     try{
         applicationInstance.initilize(GlobalContext.applicationContext,job)    
     }
     catch{
        case ex: InitializationException => {
            logger.error("Error getting while application bootup" + ex)
        }
      }
   }
}

 object BootstrapObj {
  
   def main(args: Array[String]) {
      try{
          var bootstrap = new Bootstrap(args(0))
          bootstrap.initContext();
          var isvalid = bootstrap.isValidInput()
          if(isvalid){
              bootstrap.initLogging();
              bootstrap.initProperties(args(0))
              bootstrap.boot();
             
          }else{
              printUsageAndExit(1,args(0))
          }
        }catch{
           case e: ArrayIndexOutOfBoundsException => {
              printUsageAndExit(1,"")
            }
   }
        
   def exitFn: Int => Unit = (exitCode: Int) => System.exit(exitCode)
 
   def printUsageAndExit(exitCode: Int, unknownParam: String = null): Unit = {
              // scalastyle:off println
              if (unknownParam != null) {
                println("Invalid Job Type " + unknownParam)
              }
             println(
                """Usage: bootstrap [app arguments]
                  
                  |Arguments:
                      |   attributionJobRunner
		      
                      |   dataIngestionJobRunner
                  
                """
              )
              //exitFn(1) 
    }
  
  }
  }

 
