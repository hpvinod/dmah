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
class HeuristicChannelAttributionLastClick extends HeuristicChannelAttributionBC with IHeuristicTechnique {

  private val logger = LoggerFactory.getLogger(classOf[HeuristicChannelAttributionLastClick])

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

    logger.info("Start of computeChannelWeights - Last Click")
    var joinedRDD: RDD[(String, (Iterable[UserJourney], SalesData))] = computeChannelWeightsForHeuristic(userJourneyRdd, salesRdd, investmentRdd, heuristicsWeights,
      diagnosticConfig.filteredNonConversionUserJrnyFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum, diagnosticConfig.attributionModeTest)

    logger.info("Calculating Attribution weights for Last Click")
    var channelWeightsFlatMapRdd: RDD[ChannelWeightsAtGranularLevel] = joinedRDD.flatMap(x => callLastClick(x._1, x._2._1, x._2._2, heuristicsWeights))
    logger.info("Grouping on key(UserId and subPathId")

    /*
     * The RDD channelWeightsFlatMapRdd is keyed by userID for calculating grannular level sales data and attribution weights
     */
    channelRddGranular = channelWeightsFlatMapRdd.groupBy { x: ChannelWeightsAtGranularLevel => x.userID }
    // println("channelWeightsFlatMapRdd " + channelWeightsFlatMapRdd.count() + " channelRddGranular "+channelRddGranular.count())
    writeWeightSalesGranular(diagnosticConfig.channelWtAttributionFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum, diagnosticConfig.attributionModeTest)

    /*
      * The RDD channelWeightsFlatMapRdd is keyed by channelName for calculating high level sales data and attribution weights
      * 
      * **/
    logger.info("channelWeightsFlatMapRdd is keyed by channelName for calculating high level sales data and attribution weights")
    var channelRdd = channelWeightsFlatMapRdd.keyBy(x => x.channelName)
    logger.info("sum the fraction of sales per each channel")
    var reduceByChannel: RDD[(String, ChannelWeightsAtGranularLevel)] = channelRdd.reduceByKey(addChannelWeightAcrossAllUsers) //sum the fraction of sales per each channel

    logger.info("End of computeChannelWeights - Last Click")

    return reduceByChannel

  }

  /**
   *
   * The method attributes fraction of sales value and attribution weight for the channel
   * based on last click rule. The channel that the user click just before conversion is attributed to
   * 1 as weight
   *
   * @param UserID UserID
   * @param iterUJ The Iterable Object  consisting of UserJourney associated with a UserID
   * @param sales. The sales observed for this row
   * @return channelWeightAthisUserLevel for this User ID
   *
   *
   */
  def channelWeightsCalculationLastClick(userID: String, userJourneyCollection: Iterable[UserJourney], sales: SalesData): List[ChannelWeightsAtGranularLevel] =
    {
      var userJourneyList = userJourneyCollection.to[ListBuffer]
      var listoutput = new ListBuffer[ChannelWeightsAtGranularLevel]()

      val sortedUserJourney = userJourneyList.sortBy((x) => x.timeStamp)

      var lastChannelIndex: Int = -1
      var count: Int = 0

      //var flag = (sortedUserJourney(sortedUserJourney.length - 1)).channelName.equalsIgnoreCase("conversion")

      /*var ctr : Int = -1      
      
      if (!flag) {

        for (ctr <- 0 to (sortedUserJourney.length - 2)) {
          var innerFlag = (sortedUserJourney(ctr).channelName.equalsIgnoreCase("conversion"))

          if(innerFlag && count == 0){
            lastChannelIndex = ctr
            count = count + 1
            
          }
          
        }

      }*/
      if (sortedUserJourney.length >= 2) {

        //if(flag ){
        var ChannelObj = singleChannelWeightAssignment(userID, sortedUserJourney((sortedUserJourney.length - 2)), 1, sales.salesValue)

        listoutput = ChannelObj +: listoutput

        //}
        /*else{
          
          var ChannelObj = singleChannelWeightAssignment(userID , sortedUserJourney(lastChannelIndex -1), 1 , sales.salesValue)

        listoutput = ChannelObj +: listoutput
        }*/

        return listoutput.toList

      } else {
        return null

      }

    }

  /**
   *
   * This method  delegates the call to LastClick Or PositionBased rules based on class member whichMethod value
   * @param UserID UserID
   * @param iterUJ Iterable of UserJourney object
   * @param sales Sales for this row
   * @return List of channelWeightAtUserlevel objects
   */
  def callLastClick(userID: String, userJourneyCollection: Iterable[UserJourney], sales: SalesData, assignedWeights: HeuristicsWeightsAssignment): List[ChannelWeightsAtGranularLevel] = {

    var channelWeightsList: List[ChannelWeightsAtGranularLevel] = null

    var userJourneySinglePath = findSinglePath(userID, userJourneyCollection)
    channelWeightsList = channelWeightsCalculationLastClick(userID, userJourneySinglePath.toIterable, sales)

    channelWeightsList

  }

}