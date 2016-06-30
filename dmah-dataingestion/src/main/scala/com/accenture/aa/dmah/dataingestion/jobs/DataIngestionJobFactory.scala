package com.accenture.aa.dmah.dataingestion.jobs

import scala.beans.BeanProperty
import com.accenture.aa.dmah.core.IJobRunner
import com.accenture.aa.dmah.core.DMAHJob
import com.accenture.aa.dmah.exception.JobException
 
/**
 * Factory class to initialize data ingestion job
 * 
 * @author payal.patel
 */
class DataIngestionJobFactory extends IJobRunner{
  
  @BeanProperty
  var dataIngestionJob:DataIngestionJob = _
  
  @BeanProperty
  var propertiesFileName:String=_
  /**
   * Method to create object of data ingestion job
   */
  @throws(classOf[JobException])
  def createObject: DMAHJob = dataIngestionJob  
}