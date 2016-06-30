package com.accenture.aa.dmah.attribution.modelling

import java.io._

import util.Random.nextInt
import scala.collection.mutable.ListBuffer

import org.apache.log4j.LogManager
import org.slf4j.LoggerFactory
import org.apache.spark.rdd.RDD
import com.accenture.aa.dmah.attribution.channelattribution.bo.HeuristicsWeightsAssignment
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData

/**
 * @author s.swaroop.mohanty
 * This class contains logic to compute Heuristic position based and last click.
 */
class HeuristicPositionBased extends Serializable {

  private val logger = LoggerFactory.getLogger(classOf[HeuristicPositionBased])

  var whichMethod: Int = -1

  /**
   * This method contains the logic to transform the rdd and calculate ROI
   * @param inputrdd The rdd that contains UserJourney data send according to c3 data
   * @param salesRdd The sales rdd that contain UseriD ,sales , timestamp as each row
   * @param investmentRdd The investment value for each channel
   */
  def computeChannelWeights(userJourneyRdd: RDD[UserJourney], salesRdd: RDD[SalesData], investmentRdd: RDD[Investment],
                            heuristicsWeights: HeuristicsWeightsAssignment): RDD[(String, ChannelWeightsAtGranularLevel)] = {

    logger.info("Start of computeChannelWeights")

    var groupedRDD: RDD[(String, Iterable[UserJourney])] = userJourneyRdd.groupBy { x => x.userId } // group UJourney RDD by UserID

    var filteredRdd: RDD[(String, Iterable[UserJourney])] = groupedRDD.filter(x => filterOutnonConversion(x._1, x._2)) //filter out non conversions from groupedRDD

    var pairedSalesRdd: RDD[(String, SalesData)] = salesRdd.keyBy { x => x.userId } //key the sales by user ID

    var salesCollect: Array[(String, SalesData)] = pairedSalesRdd.collect()

    var joinedRDD: RDD[(String, (Iterable[UserJourney], SalesData))] = filteredRdd.join(pairedSalesRdd) //join filteredRdd with pairedSalesRdd based on User ID as key

    /*
     * TODO:Comments
     */
    if (false) {
      createSalesInputData(filteredRdd)
    }

    /*
     * 
     * create flat rdd consisiting of ChannelWeightsAtUserlevel objects list and userid
     * 
     * x._1 contains the userID , x._2._1 contains  iterables of userJourney , X._2._2 contains sales value for that userID
     */
    var channelWeightsFlatMapRdd: RDD[ChannelWeightsAtGranularLevel] = joinedRDD.flatMap(x => delegateHeuristicsType(x._1, x._2._1, x._2._2, heuristicsWeights))

    var channelRdd = channelWeightsFlatMapRdd.keyBy(x => keyFunc(x))

    var reduceByChannel: RDD[(String, ChannelWeightsAtGranularLevel)] = channelRdd.reduceByKey(addChannelWeightAcrossAllUsers) //sum the fraction of sales per each channel

    logger.info("End of computeChannelWeights")

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
      logger.info("Inside of ChannelWeightsCalculationLastClick")

      var userJourneyList = userJourneyCollection.to[ListBuffer]
      var listoutput = new ListBuffer[ChannelWeightsAtGranularLevel]()

      val sortedUserJourney = userJourneyList.sortBy((x) => x.timeStamp)

      var flag = (sortedUserJourney(sortedUserJourney.length - 1)).channelName.equalsIgnoreCase("conversion")

      if (!flag) {

        for (ctr <- 0 to (sortedUserJourney.length - 2)) {
          var innerFlag = (sortedUserJourney(ctr).channelName.equalsIgnoreCase("conversion"))

        }

      }
      if (sortedUserJourney.length >= 2) {
        var ChannelObj = singleChannelWeightAssignment(sortedUserJourney(sortedUserJourney.length - 2), 1, sales.salesValue)

        listoutput = ChannelObj +: listoutput

        logger.info("End of ChannelWeightsCalculationLastClick")
        listoutput.toList

      } else {
        logger.info("End of ChannelWeightsCalculationLastClick")
        listoutput.toList

      }

    }

  /**
   *
   * This method associates sales and attribution weights at user level
   * @param objUserJourney UserJourney Object
   * @param weightForThisChannel weight to be assigned to this channel
   * @param salesValue The sales value to be assigned to this channel
   * @return channelWeightAtUserLevel
   */
  def singleChannelWeightAssignment(objUserJourney: UserJourney, channelWeight: Double, salesValue: Double): ChannelWeightsAtGranularLevel = {

    var channelWeightGranularLevel = new ChannelWeightsAtGranularLevel()

    channelWeightGranularLevel.channelName = objUserJourney.channelName
    channelWeightGranularLevel.channelWeight = channelWeight
    channelWeightGranularLevel.attributedSales = salesValue

    channelWeightGranularLevel
  }

  /**
   *
   * This method  delegates the call to LastClick Or PositionBased rules based on class member whichMethod value
   * @param UserID UserID
   * @param iterUJ Iterable of UserJourney object
   * @param sales Sales for this row
   * @return List of channelWeightAtUserlevel objects
   */
  def delegateHeuristicsType(userID: String, userJourneyCollection: Iterable[UserJourney], sales: SalesData, assignedWeights: HeuristicsWeightsAssignment): List[ChannelWeightsAtGranularLevel] = {
    var channelWeightsList: List[ChannelWeightsAtGranularLevel] = null
    if (whichMethod == 0) {
      channelWeightsList = channelWeightsCalculationLastClick(userID, userJourneyCollection, sales)

    } else if (whichMethod == 1) {
      channelWeightsList = positionBasedChannelWeightsCalculation(userID, userJourneyCollection, sales, assignedWeights)

    }
    channelWeightsList

  }

  //*****************************************************

  /**
   * This method computes attribution weights and fraction of sales for channel in this rows
   * This method attributes 0.3 to originator channel 0.5 to channelBefore conversion and 0.2 to intermediate rows
   * @param UserID
   * @param iterUJ
   * @param sales
   * @return
   */
  def positionBasedChannelWeightsCalculation(userID: String, userJourneyCollection: Iterable[UserJourney], sales: SalesData, assignedWeights: HeuristicsWeightsAssignment): List[ChannelWeightsAtGranularLevel] =
    {
      logger.info("Start of PositionBasedChannelWeightsCalculation")

      var userJourneyList = userJourneyCollection.to[ListBuffer]
      var channelWeightsList = new ListBuffer[ChannelWeightsAtGranularLevel]()

      /*
       * User journey sorted on time
       */
      val sortedUserJourney: ListBuffer[UserJourney] = userJourneyList.sortBy((x) => x.timeStamp)

      /*
       * for direct conversion case
       */
      if (sortedUserJourney.length == 1) {
        return null

      }

      /*
       * if there is one channel in the user path 
       */
      if (sortedUserJourney.length == 2) {

        var heuristicsWeight = new ChannelWeightsAtGranularLevel()

        val channelWeight = 1

        heuristicsWeight.channelName = sortedUserJourney(0).channelName
        heuristicsWeight.channelWeight = channelWeight

        //var ChannelObj = singleChannelWeightAssignment(sortedUserJourney(0), 1, sales.salesValue)
        //listoutput = ChannelObj +: listoutput

      } /*
       * if there is 2 channel on the user path
       */ else if (sortedUserJourney.length == 3) {

        var weightDifference = assignedWeights.intermediateWeight / 2
        var originatorWeight = assignedWeights.originatorWeight + weightDifference
        var convertorWeight = assignedWeights.convertorWeight + weightDifference

        var fractionSalesStart = (sales.salesValue) * originatorWeight

        var originator = singleChannelWeightAssignment(sortedUserJourney(0), originatorWeight, fractionSalesStart)
        channelWeightsList = originator +: channelWeightsList

        var fractionSalesLast = (sales.salesValue) * convertorWeight
        var convertor = singleChannelWeightAssignment(sortedUserJourney(1), convertorWeight, fractionSalesLast)
        channelWeightsList = convertor +: channelWeightsList
      } /*
       * if there is 3 or more channels in the user path
       */ else if (sortedUserJourney.length >= 4) {

        var fractionOfSalesStart = (sales.salesValue) * assignedWeights.originatorWeight
        var originator = singleChannelWeightAssignment(sortedUserJourney(0), assignedWeights.originatorWeight, fractionOfSalesStart)
        channelWeightsList = originator +: channelWeightsList //store the channelObjStart in the listoutput ListBuffer

        var fractionOfSalesEnd = (sales.salesValue) * assignedWeights.convertorWeight
        var convertor = singleChannelWeightAssignment(sortedUserJourney(sortedUserJourney.length - 2), assignedWeights.convertorWeight, fractionOfSalesEnd)
        channelWeightsList = convertor +: channelWeightsList

        val intermediateChannelLength = sortedUserJourney.length - 2

        var intermediateWeight = (assignedWeights.intermediateWeight) / intermediateChannelLength

        var salesIntermediate = (sales.salesValue) * intermediateWeight
        for (index <- 1 to sortedUserJourney.length - 3) {

          var intermediateObj = singleChannelWeightAssignment(sortedUserJourney(index), intermediateWeight, salesIntermediate)
          channelWeightsList = intermediateObj +: channelWeightsList
        }
      }

      logger.info("End of PositionBasedChannelWeightsCalculation")
      channelWeightsList.toList
    }

  /**
   * @param first first ChannelWeightsAtUserlevel
   * @param sec	second ChannelWeightsAtUserlevel
   * @return ChannelWeightsAtUserlevel after adding the weights and sales
   */
  def addChannelWeightAcrossAllUsers(first: ChannelWeightsAtGranularLevel, sec: ChannelWeightsAtGranularLevel): ChannelWeightsAtGranularLevel = {

    var channelWeightAtGranularLevel = new ChannelWeightsAtGranularLevel()

    channelWeightAtGranularLevel.channelName = first.channelName
    //channelWeightAtGranularLevel.channelWeight = first.channelWeight + sec.channelWeight
    channelWeightAtGranularLevel.attributedSales = first.attributedSales + sec.attributedSales
    return channelWeightAtGranularLevel

  }
  def keyFunc(obj: ChannelWeightsAtGranularLevel): String = {

    return obj.channelName

  }

  def show(obj: ChannelWeightsAtGranularLevel): String = {

    return obj.channelName

  }

  /**
   * The method filters out rows from the calling Rdd based on conversion
   * The rows that contain conversion are retained while others are removed
   * after execution of the method the converted rows are retained in the rdd while other rows are
   * discarded
   * @param UserID
   * @param iterUJ
   * @return boolean True if the row contains conversion false otherwise
   */
  def filterOutnonConversion(UserID: String, iterUJ: Iterable[UserJourney]): Boolean = {
    logger.info("Start of filterOutnonConversion")
    var listbuff = iterUJ.to[ListBuffer]
    var listoutput = new ListBuffer[ChannelWeightsAtGranularLevel]()

    // val sortedTasks = listbuff.sortBy((x) => x.timeStamp) //sort

    // var channel_name = (listbuff(listbuff.length-1).channelName)
    // var flag = (channel_name == ("conversion"))

    var len = (listbuff.length - 1)
    for (ctr <- 1 to len) {

      var objUserJourney = listbuff(ctr)

      if (objUserJourney.channelName.equalsIgnoreCase("conversion")) {

        logger.info("End of filterOutnonConversion")
        return true
      }
    }
    /* 
    if (flag == true){
      return true     
    }
    else{
      return true      
    }
     
    return true*/
    logger.info("End of filterOutnonConversion")
    return false
  }

  def createSalesInputData(filteredRdd: RDD[(String, Iterable[UserJourney])]) {

    var salesList = new ListBuffer[SalesData]()

    var noOfConvertedRows = filteredRdd.count()

    var totalConverted_List = filteredRdd.collect()
    var karr = totalConverted_List
    val file = new File("c:/SalesNewVersion2.csv")
    val bw = new BufferedWriter(new FileWriter(file))

    for (ctr <- 0 to totalConverted_List.length - 1) {

      var iterUJ: Iterable[UserJourney] = karr(ctr)._2

      var listbuff = iterUJ.to[ListBuffer]

      for (ctr <- 0 to listbuff.length - 1) {

        var objUserJourney = listbuff(ctr)

        if (objUserJourney.channelName.equalsIgnoreCase("conversion")) {
          var tempSalesData = new SalesData()
          tempSalesData.userId = objUserJourney.userId
          tempSalesData.timeStamp = objUserJourney.timeStamp
          var Rno = scala.util.Random

          tempSalesData.salesValue = Rno.nextInt(100000)
          salesList = tempSalesData +: salesList

        }
      }
    }

    for (i <- 0 to salesList.length - 1) {

      var salesObj = salesList(i)
      bw.write(salesObj.userId + "," + salesObj.timeStamp + "," + salesObj.salesValue)
      bw.newLine()
      println(" Sales Data UID : " + salesObj.userId + " sales Value :  " + salesObj.salesValue)

    }

    bw.close()
  }

}