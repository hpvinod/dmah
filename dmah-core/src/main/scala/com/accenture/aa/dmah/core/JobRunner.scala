package com.accenture.aa.dmah.core

/**
 * Base class responsible to trigger the job
 * 
 * @author payal.patel
 */
class JobRunner (factory:IJobRunner){
  
  
   val jobRunner = factory.createObject
   /**
    * *It will execute the jobs
    */
   jobRunner.execute()
}