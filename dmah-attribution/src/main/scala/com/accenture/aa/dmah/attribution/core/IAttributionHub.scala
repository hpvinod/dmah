package com.accenture.aa.dmah.attribution.core

import com.accenture.aa.dmah.attribution.core.bo.AttributionCommand

trait IAttributionHub {

  def executeAttribution(attributionCommand: AttributionCommand): Unit
}