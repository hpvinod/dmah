package com.accenture.aa.dmah.attribution.channelattribution

import com.accenture.aa.dmah.attribution.channelattribution.bo.AttributionResults
import com.accenture.aa.dmah.attribution.modelling.bo.ModellingResults
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import com.accenture.aa.dmah.spark.core.SparkContainer

abstract class AbstractChannelAttribution extends IChannelAttribution {

  def getAttributionWeights(modellingResults: Object): AttributionResults

  def getROI(attributionResults: AttributionResults): AttributionResults
}