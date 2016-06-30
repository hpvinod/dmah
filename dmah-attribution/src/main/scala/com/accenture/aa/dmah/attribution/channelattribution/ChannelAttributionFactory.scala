package com.accenture.aa.dmah.attribution.channelattribution

import com.accenture.aa.dmah.attribution.util.AttributionTechnique
import scala.beans.BeanProperty

class ChannelAttributionFactory extends IChannelAttributionFactory {

  @BeanProperty
  var heuristicsChannelAttribution: HeuristicsChannelAttribution = _

  @BeanProperty
  var logisticChannelAttribution: LogisticChannelAttribution = _

  @BeanProperty
  var markovChannelAttribution: MarkovChannelAttribution = _

  @BeanProperty
  var randomForestChannelAttribution: RandomForestChannelAttribution = _

  def createInstance(technique: String): IChannelAttribution = {

    var channelAttribution: IChannelAttribution = null

    val tech = technique match {

      case AttributionTechnique.LOGISTICS =>
        channelAttribution = logisticChannelAttribution

      case AttributionTechnique.HEURISTICS =>
        channelAttribution = heuristicsChannelAttribution

      case AttributionTechnique.MARKOV =>
        channelAttribution = markovChannelAttribution

      case AttributionTechnique.RANDOM_FOREST =>
        channelAttribution = randomForestChannelAttribution
    }

    channelAttribution

  }
}