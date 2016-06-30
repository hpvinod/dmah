package com.accenture.aa.dmah.attribution.core

import com.accenture.aa.dmah.attribution.exceptions.AttributionException

/**
 * @author vivek.gupt
 *
 */
trait AttributionHubFactory {

  def createInstance(): Unit

}