package com.accenture.aa.dmah.attribution.channelattribution

import java.io._
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig

import util.Random.nextInt
import scala.collection.mutable.ListBuffer

import org.apache.log4j.LogManager
import org.slf4j.LoggerFactory
import org.apache.spark.rdd.RDD

import com.accenture.aa.dmah.attribution.channelattribution.bo.HeuristicsWeightsAssignment

import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney

import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment

/**
 * @author s.swaroop.mohanty
 * This class contains logic to compute Heuristic position based and last click.
 */
class HeuristicChannelAttributionPositionBased extends HeuristicChannelAttributionBC with IHeuristicTechnique {

  private val logger = LoggerFactory.getLogger(classOf[HeuristicChannelAttributionPositionBased])

  @BeanProperty
  var diagnosticConfig: DiagnosticConfig = _

  /**
   * This method contains the logic to transform the rdd and calculate ROI
   * @param inputrdd The rdd that contains UserJourney data send according to c3 data
   * @param salesRdd The sales rdd that contain UseriD ,sales , timestamp as each row
   * @param investmentRdd The investment value for each channel
   */
  def computeChannelWeights(userJourneyRdd: RDD[UserJourney], salesRdd: RDD[SalesData], investmentRdd: RDD[Investment],
                            heuristicsWeights: HeuristicsWeightsAssignment): RDD[(String, ChannelWeightsAtGranularLevel)] = {

    logger.info("Joining Sales and UserJourney RDD")
    var joinedRDD: RDD[(String, (Iterable[UserJourney], SalesData))] = computeChannelWeightsForHeuristic(userJourneyRdd, salesRdd, investmentRdd, heuristicsWeights,
      diagnosticConfig.filteredNonConversionUserJrnyFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum, diagnosticConfig.attributionModeTest)

    logger.info("Calculating Attribution weights for Position Based")
    var channelWeightsFlatMapRdd: RDD[ChannelWeightsAtGranularLevel] = joinedRDD.flatMap(x => callPositionBased(x._1, x._2._1, x._2._2, heuristicsWeights))
    logger.info("Grouping on key(UserId and subPathId")
    channelRddGranular = channelWeightsFlatMapRdd.groupBy { x: ChannelWeightsAtGranularLevel => x.userID }
    writeWeightSalesGranular(diagnosticConfig.channelWtAttributionFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum, diagnosticConfig.attributionModeTest)

    logger.info("channelWeightsFlatMapRdd is keyed by channelName for calculating high level sales data and attribution weights")
    var channelRdd = channelWeightsFlatMapRdd.keyBy(x => x.channelName)
    logger.info("sum the fraction of sales per each channel")
    var reduceByChannel: RDD[(String, ChannelWeightsAtGranularLevel)] = channelRdd.reduceByKey(addChannelWeightAcrossAllUsers) //sum the fraction of sales per each channel

    logger.info("End of computeChannelWeights for Position Based")

    return reduceByChannel

  }

  /**
   *
   * This method  delegates the call to LastClick Or PositionBased rules based on class member whichMethod value
   * @param UserID UserID
   * @param iterUJ Iterable of UserJourney object
   * @param sales Sales for this row
   * @return List of channelWeightAtUserlevel objects
   */
  def callPositionBased(userID: String, userJourneyCollection: Iterable[UserJourney], sales: SalesData, assignedWeights: HeuristicsWeightsAssignment): List[ChannelWeightsAtGranularLevel] = {
    var channelWeightsList: List[ChannelWeightsAtGranularLevel] = null
    var UserJourneySinglePath = findSinglePath(userID, userJourneyCollection)
    channelWeightsList = positionBasedChannelWeightsCalc(userID, UserJourneySinglePath.toIterable, sales, assignedWeights)

    channelWeightsList

  }

  //*****************************************************

  /**
   * This method computes attribution weights and fraction of sales for channel in this rows
   * This method attributes 0.3 to originator channel 0.5 to converter and 0.2 to intermediate rows
   * @param UserID
   * @param iterUJ
   * @param sales
   * @return
   */
  def positionBasedChannelWeightsCalc(userID: String, userJourneyCollection: Iterable[UserJourney], sales: SalesData, assignedWeights: HeuristicsWeightsAssignment): List[ChannelWeightsAtGranularLevel] =
    {
      //      logger.info("Start of PositionBasedChannelWeightsCalculation")

      var userJourneyList = userJourneyCollection.to[ListBuffer]
      var channelWeightsList = new ListBuffer[ChannelWeightsAtGranularLevel]()

      val sortedUserJourney: ListBuffer[UserJourney] = userJourneyList.sortBy((x) => x.timeStamp)

      if (sortedUserJourney.length == 1) {
        return null

      }

      if (sortedUserJourney.length == 2) {

        var heuristicsWeight = new ChannelWeightsAtGranularLevel()

        val channelWeight = 1

        heuristicsWeight.channelName = sortedUserJourney(0).channelName
        heuristicsWeight.channelWeight = channelWeight
        var flag = (sortedUserJourney(0).channelName.equalsIgnoreCase("conversion"))
        if (flag) {
          return null

        }

        var ChannelObj = singleChannelWeightAssignment(userID, sortedUserJourney(0), 1, sales.salesValue)
        channelWeightsList = ChannelObj +: channelWeightsList

      } else if (sortedUserJourney.length == 3) {

        var weightDifference = assignedWeights.intermediateWeight / 2
        var originatorWeight = assignedWeights.originatorWeight + weightDifference
        var convertorWeight = assignedWeights.convertorWeight + weightDifference

        var fractionSalesStart = (sales.salesValue) * originatorWeight

        var originator = singleChannelWeightAssignment(userID, sortedUserJourney(0), originatorWeight, fractionSalesStart)
        channelWeightsList = originator +: channelWeightsList

        var fractionSalesLast = (sales.salesValue) * convertorWeight
        var convertor = singleChannelWeightAssignment(userID, sortedUserJourney(1), convertorWeight, fractionSalesLast)
        channelWeightsList = convertor +: channelWeightsList
      } else if (sortedUserJourney.length >= 4) {

        var fractionOfSalesStart = (sales.salesValue) * assignedWeights.originatorWeight
        var originator = singleChannelWeightAssignment(userID, sortedUserJourney(0), assignedWeights.originatorWeight, fractionOfSalesStart)
        channelWeightsList = originator +: channelWeightsList //store the channelObjStart in the listoutput ListBuffer

        var fractionOfSalesEnd = (sales.salesValue) * assignedWeights.convertorWeight
        var convertor = singleChannelWeightAssignment(userID, sortedUserJourney(sortedUserJourney.length - 2), assignedWeights.convertorWeight, fractionOfSalesEnd)
        channelWeightsList = convertor +: channelWeightsList

        val intermediateChannelLength = sortedUserJourney.length - 3

        var intermediateWeight = (assignedWeights.intermediateWeight) / intermediateChannelLength

        var salesIntermediate = (sales.salesValue) * intermediateWeight
        for (index <- 1 to sortedUserJourney.length - 3) {

          var intermediateObj = singleChannelWeightAssignment(userID, sortedUserJourney(index), intermediateWeight, salesIntermediate)
          channelWeightsList = intermediateObj +: channelWeightsList
        }
      }

      //    logger.info("End of PositionBasedChannelWeightsCalculation")
      channelWeightsList.toList
    }

}