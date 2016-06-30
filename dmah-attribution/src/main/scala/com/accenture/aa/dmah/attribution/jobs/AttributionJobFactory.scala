package com.accenture.aa.dmah.attribution.jobs

import scala.beans.BeanProperty
import com.accenture.aa.dmah.exception.JobException
import com.accenture.aa.dmah.core.IJobRunner
import com.accenture.aa.dmah.core.DMAHJob

/**
 * This class responsible to create Attribution job
 *
 * @author payal.patel
 *
 */

class AttributionJobFactory extends IJobRunner{
   @BeanProperty
   var attributionJob: AttributionJob = _
   
   @BeanProperty
   var propertiesFileName:String=_
   
   @throws(classOf[JobException])
   def createObject: DMAHJob = attributionJob
}