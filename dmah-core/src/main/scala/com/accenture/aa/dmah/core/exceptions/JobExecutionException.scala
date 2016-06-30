package com.accenture.aa.dmah.exception

/**
 * Exception class used during job execution.
 * 
 * @author payal.patel
 *
 */
class JobExecutionException(message: String, nestedException: Throwable) extends JobException(message, nestedException) {
    def this() = this("", null)
     
    def this(message: String) = this(message, null)
     
    def this(nestedException : Throwable) = this("", nestedException)
 }