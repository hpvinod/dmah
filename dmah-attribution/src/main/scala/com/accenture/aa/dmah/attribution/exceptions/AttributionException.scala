package com.accenture.aa.dmah.attribution.exceptions

import com.accenture.aa.dmah.exception.DMAHBaseException

class AttributionException(message: String, nestedException: Throwable) extends DMAHBaseException(message, nestedException) {

  def this() = this("", null)

  def this(message: String) = this(message, null)

  def this(nestedException: Throwable) = this("", nestedException)
}