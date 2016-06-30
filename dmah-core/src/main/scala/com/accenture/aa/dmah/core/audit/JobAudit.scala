package com.accenture.aa.dmah.core.audit

import java.util.Date
import org.apache.commons.lang3.builder.ReflectionToStringBuilder

/**
 * Class used to do auditing for Running job
 *  
 * @author payal.patel
 */
class JobAudit {
  
  var creationdate: Date = _
  var startTime:Long = _
  var jobId: String = java.util.UUID.randomUUID.toString
  var jobType: String = _
  var jobEndTime: Date = _
  var jobStatus: String = JobStatus.NOTSTARTED.toString()
 
  var jobTotalTime:Long = _
  
  override def toString = {
		 ReflectionToStringBuilder.toString(this);
	}
}