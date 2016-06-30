package com.accenture.aa.dmah.core

import org.apache.log4j.xml.DOMConfigurator
import org.springframework.context.ApplicationContext
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory

import com.accenture.aa.dmah.core.loaders.ILoader
import com.accenture.aa.dmah.core.loaders.LoaderList

import com.accenture.aa.dmah.core.exceptions.InitializationException
import com.accenture.aa.dmah.core.util.ApplicationUtil

import com.accenture.aa.dmah.core.exceptions.InitializationException
import com.accenture.aa.dmah.dataingestion.jobs.DataIngestionJobFactory
import com.accenture.aa.dmah.attribution.jobs.AttributionJobFactory
  

/**
 * This singleton class used to load loaders like sparkloader and initiate job.
 * 
 * @author Kanwar.singh
 */
class ApplicationInstance {
  
  
   var configRunner = new ConfigRunner()
   
  /**
   * This method will initialize loaders and job
   * Runners as per job type
   * */
  
  @throws(classOf[InitializationException])
	def initilize(context:ApplicationContext,job:String) = {
    
    /**
      * This will set application context and spark context globally
      */
    GlobalContext.applicationContext = context
    configRunner.configure()
	   
	  new JobRunner(createJobFactory(job))
	}
  
  /**
   * This method will create job factory as per job type
   * */
  
  @throws(classOf[InitializationException])
  def createJobFactory(jobType: String): IJobRunner = {
    ApplicationUtil.retrieveBean(jobType).asInstanceOf[IJobRunner]
  }
 
}

 /**
	 * Class to load loaders
	 */
	class ConfigRunner(){
    
	  val logger = LoggerFactory.getLogger(classOf[ConfigRunner])  
	  
    @throws(classOf[InitializationException])
	  def configure(){
	   initilizeLoaders()
    }
	  
	  
	  /**
     * This method used to load loader class configured in xml
     */
    def initilizeLoaders () = {
      logger.info("initializing Loaders..................")
      
      val bean = ApplicationUtil.retrieveBean("onInitializationLoaders").asInstanceOf[LoaderList]
      for(loader <- bean.list){
       loader.execute()
     } 
    }
  }