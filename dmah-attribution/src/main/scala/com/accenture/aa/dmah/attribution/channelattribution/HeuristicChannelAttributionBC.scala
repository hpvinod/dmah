package com.accenture.aa.dmah.attribution.channelattribution

import java.io._

import util.Random.nextInt
import scala.collection.mutable.ListBuffer

import org.apache.log4j.LogManager
import org.slf4j.LoggerFactory
import org.apache.spark.rdd.RDD
import com.accenture.aa.dmah.attribution.channelattribution.bo.HeuristicsWeightsAssignment
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.core.HdfsWriter
import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig

/**
 * @author s.swaroop.mohanty
 * This class contains logic to compute Heuristic position based and last click.
 */
abstract class HeuristicChannelAttributionBC extends Serializable with IHeuristicTechnique {

  private val logger = LoggerFactory.getLogger(classOf[HeuristicChannelAttributionBC])

  //@BeanProperty
  var whichMethod: Int = _

  var channelRddGranular: RDD[(String, Iterable[ChannelWeightsAtGranularLevel])] = null

  /**
   * This method contains the logic to transform the rdd and calculate ROI
   * @param inputrdd The rdd that contains UserJourney data send according to c3 data
   * @param salesRdd The sales rdd that contain UseriD ,sales , timestamp as each row
   * @param investmentRdd The investment value for each channel
   */
  def computeChannelWeightsForHeuristic(userJourneyRdd: RDD[UserJourney], salesRdd: RDD[SalesData], investmentRdd: RDD[Investment],
                                        heuristicsWeights: HeuristicsWeightsAssignment, fileName: String, folder: String, partitions: Integer, attributionModeTest: Boolean): RDD[(String, (Iterable[UserJourney], SalesData))] = {

    logger.info("Start of computeChannelWeightsForHeuristic")
    var groupedRDD: RDD[(String, Iterable[UserJourney])] = userJourneyRdd.groupBy { x => x.userId } // group UJourney RDD by UserID

    var filteredRdd: RDD[(String, Iterable[UserJourney])] = groupedRDD.filter(x => filterOutnonConversion(x._1, x._2)) //filter out non conversions from groupedRDD
    if (attributionModeTest) {
      var flatRDD = filteredRdd.flatMap(x => x._2)
      HdfsWriter.writeRDDToHdfs(flatRDD, fileName, folder, partitions)
    }
    var pairedSalesRdd: RDD[(String, SalesData)] = salesRdd.keyBy { x => x.userId } //key the sales by user ID

    var joinedRDD: RDD[(String, (Iterable[UserJourney], SalesData))] = filteredRdd.join(pairedSalesRdd) //join filteredRdd with pairedSalesRdd based on User ID as key

    if (joinedRDD.count() == 0) {
      logger.info("userid doesn't match into user journey dataset & sales dataset")
    }

    /*
     * TODO:It should come from spring
     */
    if (false) {
      createSalesInputData(filteredRdd)
    }

    logger.info("End of computeChannelWeightsForHeuristic")
    return joinedRDD
  }

  /**
   * The method prints the sales and weights of all converted users at granular level
   */
  def writeWeightSalesGranular(fileName: String, folder: String, partitions: Integer, attributionModeTest: Boolean) = {

    logger.info("Start writing granular level attribution results")
    if (attributionModeTest) {
      var flatRDD = channelRddGranular.flatMap(x => x._2)
      HdfsWriter.writeRDDToHdfs(flatRDD, fileName, folder, partitions)
    }

    /*   var granularDataList =  channelRddGranular.collect()
     val myfile = new File("C:/LastClickweightSales.csv")
    val bw = new BufferedWriter(new FileWriter(myfile))
   for(ctr<- 0 to granularDataList.length-1 ){
     
     var lst = granularDataList(ctr)._2.toList
     var len = lst.length 
     
     for(ltr <- 0 to len -1){
       bw.write(granularDataList(ctr)._1);
       bw.write(","+lst(ltr).channelName + ","+lst(ltr).channelWeight+","+lst(ltr).attributedSales)
       bw.newLine()
       bw.flush()
     }
     
   }
   
   bw.close()*/
    logger.info("Start writing granular level attribution results")
  }

  /**
   *
   * This method associates sales and attribution weights at user level
   * @param objUserJourney UserJourney Object
   * @param weightForThisChannel weight to be assigned to this channel
   * @param salesValue The sales value to be assigned to this channel
   * @return channelWeightAtUserLevel
   */
  def singleChannelWeightAssignment(UserID: String, objUserJourney: UserJourney, channelWeight: Double, attributedSales: Double): ChannelWeightsAtGranularLevel = {

    var channelWeightGranularLevel = new ChannelWeightsAtGranularLevel()

    channelWeightGranularLevel.channelName = objUserJourney.channelName
    channelWeightGranularLevel.channelWeight = channelWeight
    channelWeightGranularLevel.attributedSales = attributedSales
    channelWeightGranularLevel.userID = UserID

    channelWeightGranularLevel
  }

  /**
   * @param userID
   * @param userJourneyCollection it is the Iterable of UserJourney that contains conversion with in the Iterable
   * at least once.
   * @return List of userJourney objects that belong to the first Subpath and the list ends with a conversion
   */
  def findSinglePath(userID: String, userJourneyCollection: Iterable[UserJourney]): List[UserJourney] = {

    logger.debug("Start of findSinglePath")

    var userJourneyList = userJourneyCollection.to[ListBuffer]
    var channelWeightsList = new ListBuffer[UserJourney]()

    /*
       * User journey sorted on time 
       */
    val sortedUserJourney: ListBuffer[UserJourney] = userJourneyList.sortBy((x) => x.timeStamp)

    for (ctr <- 0 to sortedUserJourney.length - 1) {

      var userJourneyObj = sortedUserJourney(ctr)

      if (userJourneyObj.channelName.equalsIgnoreCase("conversion")) {

        channelWeightsList = userJourneyObj +: channelWeightsList
        return channelWeightsList.toList

      } else {

        channelWeightsList = userJourneyObj +: channelWeightsList

      }

    }
    logger.debug("Start of findSinglePath")

    return null

  }

  /**
   * @param first first ChannelWeightsAtUserlevel
   * @param sec	second ChannelWeightsAtUserlevel
   * @return ChannelWeightsAtUserlevel after adding the weights and sales. This method is supposed to be called from reduceByKey method to sum
   * the channel weights across all users
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

  /*def granularFunc(obj: ChannelWeightsAtGranularlevel): Unit = {

    return obj.userID

  }*/

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

    var listbuff = iterUJ.to[ListBuffer]

    if (listbuff.length == 1) {
      return false
    }
    var len = (listbuff.length - 1)
    for (ctr <- 0 to len) {

      var objUserJourney = listbuff(ctr)

      if (objUserJourney.channelName.equalsIgnoreCase("conversion")) {

        return true
      }
    }
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