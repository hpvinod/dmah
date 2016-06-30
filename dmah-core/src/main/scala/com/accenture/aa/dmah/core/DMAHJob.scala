package com.accenture.aa.dmah.core

import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.exception.JobExecutionException
import com.accenture.aa.dmah.core.audit.JobAudit
import java.util.Calendar
import com.accenture.aa.dmah.core.audit.JobStatus
 
/**
 * This class trigger the job according to input and do necessary preinitilization and postinitilization task required by all job
 * 
 * @author payal.patel
 */
abstract class DMAHJob {
  protected val logger = LoggerFactory.getLogger(classOf[DMAHJob])
 
 // var jobAudit:JobAudit = _
 
   def execute() ={
    logger.debug("inside execute method of DMAHJob")
    var jobAudit = preProcess
    processJob(jobAudit)
    postProcess(jobAudit)
  }
  
  
  /**
   * This method will used to initialize jobAuditInstance
  */
 @throws(classOf[JobExecutionException])
  def preProcess:JobAudit ={
   
    /**
     * Populate jobAudit object
     */
    var jobAudit = populateJobAudit();
        
    logger.info(String.format("Job process for %s", jobAudit.toString()))
    logger.debug("inside preprocess method of DMAHJob")
    
    return jobAudit
  }
  
 /**
  * Method to populate jobaudit object
  */
 private def populateJobAudit():JobAudit = {
     /**
       * Populate jobAudit object
       */
      var jobAudit=new JobAudit
      jobAudit.creationdate = Calendar.getInstance.getTime
      jobAudit.startTime =  Calendar.getInstance.getTimeInMillis
      jobAudit.jobType = this.getClass.getName
      jobAudit.jobStatus = JobStatus.INITIATED.toString()
      jobAudit
    }
 

   def processJob(jobAudit:JobAudit): JobAudit = {
     
      logger.debug("inside process method of DMAHJob")
      jobAudit.jobStatus = JobStatus.RUNNING.toString()
      
      logger.info("JobStatus:"+jobAudit.jobStatus)
      try{
        process(jobAudit.jobId)
      }catch{
        case e: JobExecutionException => {
            jobAudit.jobStatus = JobStatus.ERROR.toString()
            logger.error("Exception found while running job",e)
            logger.error("*********************Error while Running job************",jobAudit.toString())
            postProcess(jobAudit)
          }
      }
            
      return jobAudit
  }
    
  @throws(classOf[JobExecutionException])
  def postProcess(jobAudit:JobAudit) ={
    
    if(jobAudit.jobStatus == JobStatus.ERROR){
       logger.info("Clean up process of DMAHJob")

    }else{
      jobAudit.jobStatus = JobStatus.COMPLETED.toString()
      jobAudit.jobEndTime = Calendar.getInstance.getTime
      
      var totalTime:Long = Calendar.getInstance.getTimeInMillis - jobAudit.startTime 
      
      jobAudit.jobTotalTime = totalTime
      
      logger.info(String.format("Job processed for %s", jobAudit.toString()))
    }
      /**
       * need to do for cleanup activity
       */
      logger.info("Clean up process of DMAHJob")
  }

  @throws(classOf[JobExecutionException])
  def process(jobId:String) ={
   logger.debug("inside process method of DMAHJob")
  }
}