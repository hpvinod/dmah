package com.accenture.aa.dmah.core.exceptions

import com.accenture.aa.dmah.exception.DMAHBaseException
/**
 * Exception class used during initialization of application.
 * 
 * @author payal.patel
 *
 */
class InitializationException(message: String, nestedException: Throwable) extends DMAHBaseException(message, nestedException) {
    def this() = this("", null)
     
    def this(message: String) = this(message, null)
     
    def this(nestedException : Throwable) = this("", nestedException)
 }