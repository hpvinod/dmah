package com.accenture.aa.dmah.dataingestion.jobs

 import com.accenture.aa.dmah.core.DMAHJob
 import org.slf4j.LoggerFactory
 import com.accenture.aa.dmah.exception.JobExecutionException
  
/**
 * This class responsible to perform business logic for Data ingestion job
 * 
 */
class DataIngestionJob extends DMAHJob{
   
 
   override def process(jobId:String) ={
      logger.info("in process of DataIngestionJob")
  }
     
}