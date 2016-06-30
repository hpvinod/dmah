package com.accenture.aa.dmah.attribution.channelattribution

import com.accenture.aa.dmah.attribution.modelling.bo.ModellingResults
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig
import com.accenture.aa.dmah.attribution.channelattribution.bo.AttributionResults
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.attribution.modelling.HeuristicPositionBased
import com.accenture.aa.dmah.attribution.channelattribution.bo.HeuristicsWeightsAssignment
import com.accenture.aa.dmah.core.GlobalContext
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment
import org.apache.spark.sql.Row
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import java.text.SimpleDateFormat
import scala.beans.BeanProperty
import org.apache.spark.rdd.RDD
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.ROI
import scala.collection.mutable.ListBuffer
import com.accenture.aa.dmah.spark.core.SparkContainer
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import com.accenture.aa.dmah.core.HdfsWriter
import org.apache.spark.sql.DataFrame

class HeuristicsChannelAttribution extends AbstractChannelAttribution with Serializable {

  private val logger = LoggerFactory.getLogger(classOf[HeuristicsChannelAttribution])

  @BeanProperty
  var heuristicsWeights: HeuristicsWeightsAssignment = _

  @BeanProperty
  var heuristicTechniqueFactory: HeuristicTechniqueFactory = _

  @BeanProperty
  var technique: String = _

  @BeanProperty
  var diagnosticConfig: DiagnosticConfig = _

  var investmentRddProcesed: RDD[Investment] = null

  var roiList = new ListBuffer[ROI]()
  
  var reduceByChannel: RDD[(String, ChannelWeightsAtGranularLevel)] = null

  override def getAttributionWeights(modellingResults: Object): AttributionResults = {

    //TODO: This should be casacaded from previous modules
    //var userJourneyDF: DataFrame = GlobalContext.userJourney
    var userJourneyDF: DataFrame = modellingResults.asInstanceOf[DataFrame]
    
    var userJourRdd = userJourneyDF.rdd
    var schemaDF = userJourneyDF.schema
    
    var salesData: DataFrame = GlobalContext.salesData
    var salesRdd = salesData.rdd

    var investment: DataFrame = GlobalContext.investment
    var investmentRdd = investment.rdd
    
    investmentRddProcesed = investmentRdd.map { processInvestment }

    var ProcessedRdd = userJourRdd.map(processUserJouneyr)
    var salesProcessedrdd = salesRdd.map { processSales }

    reduceByChannel = heuristicTechniqueFactory.createInstance(technique).computeChannelWeights(ProcessedRdd, salesProcessedrdd, investmentRddProcesed, heuristicsWeights)
    null
  }

  override def getROI(attributionResults: AttributionResults): AttributionResults = {

  

    var investCollect = investmentRddProcesed.collect()

    var totalChannelWeights = reduceByChannel.collect() //collect the final result

    var sum = 0.0
    var collectRDD = totalChannelWeights

    for (ctr <- 0 to totalChannelWeights.length - 1) { //compute ROI
      var channel_name = collectRDD(ctr)._1
      sum = totalChannelWeights(ctr)._2.channelWeight + sum
      for (itr <- 0 to investCollect.length - 1) {

        var flag = channel_name.equalsIgnoreCase(investCollect(itr).channelName)
        if (flag && (!channel_name.equalsIgnoreCase("conversion"))) {
          var roiObj = new ROI()
          roiObj.channelName = investCollect(itr).channelName
          roiObj.ROI = (collectRDD(ctr)._2.attributedSales) / (investCollect(itr).investment)
          roiList = roiObj +: roiList
        }
      }

    }
    if (diagnosticConfig.attributionModeTest) {
      HdfsWriter.writeListToHdfs(roiList, diagnosticConfig.channelROIFile, diagnosticConfig.hdfsFolder)
    }
    null
  }

  /**
   * This method converts raw Row data to userJourney objects
   * @param row The input rows of rdd that will be converted to each UserJOurney Objects
   * @return userJourney Objects
   */
  def processUserJouneyr(row: Row): UserJourney =
    {

      logger.debug("Start of process , to convert Raw rows to userJourney objects")
      var usrJourObj = new UserJourney()

      var dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

      if ((row.get(2) != null) && (row.get(0) != null) && row.get(1).toString().length >= 18) { //to check the case where the last line is splitted abruptly
        usrJourObj.channelName = row.get(2).toString()
        usrJourObj.userId = row.get(0).toString()
        var str = row.get(1).toString()
        var thisDate = dateFormat.parse(str)
        usrJourObj.timeStamp = thisDate.getTime
      }

      logger.debug("End of process methods , all the userJourney objects have been created")
      return usrJourObj
    }

  /**
   * @param row The input rows of rdd that will be converted to each sales Objects
   * @return SalesData object per each row
   */
  def processSales(row: Row): SalesData =
    {

      var salesDataObj = new SalesData()

      var str = row.toString()
      var arr = str.split(",")

      if ((row.get(2) != null) && (row.get(0) != null)) {
        salesDataObj.userId = row.get(0).toString()
        salesDataObj.timeStamp = row.get(1).toString().toLong
        salesDataObj.salesValue = row.get(2).toString().toDouble

      }
      return salesDataObj
    }

  /**
   * @param row row of one entry of the calling rdd
   * @return the investment class object
   */
  def processInvestment(row: Row): Investment =
    {

      var investmentObj = new Investment()

      var str = row.toString()
      var arr = str.split(",")

      if ((row.get(1) != null) && (row.get(0) != null)) {
        investmentObj.channelName = row.get(0).toString()
        investmentObj.investment = row.get(1).toString().toLong

      }
      return investmentObj
    }

}