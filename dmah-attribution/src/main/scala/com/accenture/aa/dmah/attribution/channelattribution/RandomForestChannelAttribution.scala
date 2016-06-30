package com.accenture.aa.dmah.attribution.channelattribution

import org.slf4j.LoggerFactory
import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.channelattribution.bo.AttributionResults
import com.accenture.aa.dmah.attribution.modelling.bo.ModellingResults
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import com.accenture.aa.dmah.spark.core.SparkContainer

class RandomForestChannelAttribution extends AbstractChannelAttribution {

  private val logger = LoggerFactory.getLogger(classOf[RandomForestChannelAttribution])

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  @BeanProperty
  var sparkContainer: SparkContainer = _

  override def getAttributionWeights(modellingResults: Object): AttributionResults = {
    null
  }

  override def getROI(attributionResults: AttributionResults): AttributionResults = {
    null
  }
}