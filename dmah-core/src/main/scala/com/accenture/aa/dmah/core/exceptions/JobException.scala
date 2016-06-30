package com.accenture.aa.dmah.exception
/**
 * Exception class used during job creation of application.
 * 
 * @author payal.patel
 *
 */
class JobException(message: String, nestedException: Throwable) extends DMAHBaseException(message, nestedException) {
    def this() = this("", null)
     
    def this(message: String) = this(message, null)
     
    def this(nestedException : Throwable) = this("", nestedException)
 }