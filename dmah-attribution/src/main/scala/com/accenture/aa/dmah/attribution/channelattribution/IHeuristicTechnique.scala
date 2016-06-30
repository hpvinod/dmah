package com.accenture.aa.dmah.attribution.channelattribution

import org.apache.spark.rdd.RDD
import com.accenture.aa.dmah.attribution.channelattribution.bo.HeuristicsWeightsAssignment
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney

trait IHeuristicTechnique {

  def computeChannelWeights(userJourneyRdd: RDD[UserJourney], salesRdd: RDD[SalesData], investmentRdd: RDD[Investment],
                            heuristicsWeights: HeuristicsWeightsAssignment): RDD[(String, ChannelWeightsAtGranularLevel)]

}