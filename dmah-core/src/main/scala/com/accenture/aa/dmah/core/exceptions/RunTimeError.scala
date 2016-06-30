package com.accenture.aa.dmah.exception
  
/**
 * This error should generally only be shown when it wont allowed checked exceptions of the appropriate type
 * to be thrown
 * 
 * @author payal.patel
 *
 */

class RuntimeError(message: String, nestedException: Throwable) extends /*Error*/Exception(message, nestedException) {
    def this() = this("", null)
     
    def this(message: String) = this(message, null)
     
    def this(nestedException : Throwable) = this("", nestedException)
}