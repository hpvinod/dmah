package com.accenture.aa.dmah.attribution.channelattribution

import com.accenture.aa.dmah.attribution.channelattribution.bo.AttributionResults

trait IChannelAttributionFactory {

  def createInstance(technique: String): IChannelAttribution
}