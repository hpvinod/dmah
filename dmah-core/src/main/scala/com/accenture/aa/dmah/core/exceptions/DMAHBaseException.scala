package com.accenture.aa.dmah.exception

/**
 * Base exception class for all checked exceptions thrown by DMAH.
 * 
 * @author payal.patel
 *
 */
class DMAHBaseException(message: String, nestedException: Throwable) extends Exception(message, nestedException) {
    def this() = this("", null)
     
    def this(message: String) = this(message, null)
     
    def this(nestedException : Throwable) = this("", nestedException)
}