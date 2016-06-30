package com.accenture.aa.dmah.attribution.channelattribution

import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.attribution.channelattribution.bo.AttributionResults
import com.accenture.aa.dmah.attribution.modelling.bo.ModellingResults
import com.accenture.aa.dmah.core.GlobalContext
import org.apache.spark.sql.Row
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import scala.beans.BeanProperty
import scalaz.std.map._
import scalaz.outlaws.std.double._
import scalaz.syntax.foldable._
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import com.accenture.aa.dmah.spark.core.SparkContainer
import com.mongodb.BasicDBObject
import com.accenture.aa.dmah.nosql.core.NoSQLInitializationInSpark
import java.util.ArrayList
import com.mongodb.DBObject
import com.accenture.aa.dmah.core.HdfsWriter
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig
import scala.collection.mutable.ListBuffer

/**
 * @author himanshu.babbar
 *
 * This class implements the attribution techniques for Logistic Regression
 */
class LogisticChannelAttribution extends AbstractChannelAttribution with Serializable {

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  @BeanProperty
  var sparkContainer: SparkContainer = _

  @BeanProperty
  var diagnosticConfig: DiagnosticConfig = _

  private val logger = LoggerFactory.getLogger(classOf[LogisticChannelAttribution])

  var granularLevelAttributionMap = Map[String, Map[String, Double]]()

  /**
   * @author himanshu.babbar
   * @param Map[String, Double]
   * @return TODO
   *
   * This method is the implementation for abstract method getAttributionWeights for LogisticRegression
   *
   * It takes a map of modeling results i.e. channels and their coefficients and
   * returns TODO
   *
   */
  override def getAttributionWeights(modellingResultMap: Object): AttributionResults = {

    logger.info("Calculating attribution weights as per requirement")

    granularLevelAttributionMap = getGranularLevelAttribution(modellingResultMap.asInstanceOf[Map[String, Double]])

    this.getGranularLevelSalesAttribution(granularLevelAttributionMap)

    //var overallLevelAttributionMap = getOverallLevelAttribution(modellingResultMap)

    logger.info("Returning attribution map")

    null
  }

  override def getROI(attributionResults: AttributionResults): AttributionResults = {

    null

  }

  def getGranularLevelSalesAttribution(granularLevelAttributionMap: Map[String, Map[String, Double]]): Unit = {

    var salesDF = GlobalContext.salesData

    var granularLevelSalesMap = Map[String, Map[String, Double]]()
    var channelSalesMap = Map[String, Double]()

    var userChannelAttributionList = new ListBuffer[String]()
    salesDF.show()
    logger.info("Sales rows count is" + salesDF.count())
    salesDF.collect().foreach { (row: Row) =>

      var userid = row.getString(0)
      var sales = row.getDouble(1)

      if (granularLevelAttributionMap.exists(_._1 == userid)) {
        var channelAttributionMap = granularLevelAttributionMap.get(userid).get

        var channelSalesMap = granularLevelAttributionMap.get(userid).get.mapValues(_ * sales)

        val batchContainer = this.sparkContainer.batchContainer

        channelAttributionMap.keySet.foreach { channel: String =>
          var userChannelSales = userid + "," + channel + "," + channelAttributionMap.get(channel).get.toString() + "," + channelSalesMap.get(channel).get.toString()

          userChannelAttributionList = userChannelSales +: userChannelAttributionList

          /* val mongoClient = batchContainer.nosqlService.nosqlClient.mongoClient;
           if(mongoClient == null){
             batchContainer.nosqlService.nosqlClient.mongoClient = NoSQLInitializationInSpark.mongoClient
           }*/
          //val array =  x.split(",")
          val dataList = new ArrayList[DBObject];
          val dbobj = new BasicDBObject()
          dbobj.append("Userid", userid).append("channelName", channel).append("attributionValue", channelAttributionMap.get(channel).get.toString()).append("attributedSales", channelSalesMap.get(channel).get.toString())
          dataList.add(dbobj)
          this.sparkContainer.batchContainer.writeData(dataList, "DMAH", "LRAttribution", null);

        }

        granularLevelSalesMap += (userid -> channelSalesMap)
      }

    }

    HdfsWriter.writeListToHdfs(userChannelAttributionList, diagnosticConfig.lrUserChannelAttribution, diagnosticConfig.hdfsFolder)

    val finalChannelSalesMap = granularLevelSalesMap.concatenate

    logger.info("The final channel sales map is " + finalChannelSalesMap)

    //TODO: Refactor this code to ROI method
    //garanularLevelSalesMap += (userid -> granularLevelAttributionMap.get(userid))
    var channelInvestmentDF = GlobalContext.investment
    var roiMap = Map[String, Double]()

    channelInvestmentDF.collect().foreach { row: Row =>

      var channelName = row.getString(0)
      var investment = row.getInt(1).toDouble

      if (!channelName.equalsIgnoreCase("Conversion")) {
        roiMap += (channelName -> finalChannelSalesMap.get(channelName).get / investment)
      }

    }
    HdfsWriter.writeMapToHdfs(roiMap, diagnosticConfig.lrChannelROI, diagnosticConfig.hdfsFolder)
    logger.info("The ROI map is " + roiMap)

  }

  /**
   * @author himanshu.babbar
   * @param Map[String, Double]
   * @return Map[String, Map[String, Double]]
   *
   * This method reads the converted data frame from GlobalContext,
   * and for each row of data (which represents one converted user path),
   * we will calculate the probability of conversion for each individual channel
   * using the formula      *******************************************************************************
   *                        *   P(Channel | Rest of the channels) = P(All Channels) - P(Rest of channels)  *
   *                        *                                                                              *
   *                        *                                                                              *
   *                        *******************************************************************************
   */
  def getGranularLevelAttribution(modellingResultMap: Map[String, Double]): Map[String, Map[String, Double]] = {

    logger.info("Calculating Granular Level attribution")

    logger.info("The converted data frame is : \n ")
    val convertedPaths = GlobalContext.dataFrameTuple._1
    convertedPaths.show()
    logger.info("Number of converted rows is " + convertedPaths.count())

    var channelSet = modellingResultMap.keySet
    channelSet -= "Intercept"

    logger.info("The modeling result map is " + modellingResultMap + "\n And the set of channels is " + channelSet)

    var channelFrequencyMap = Map[String, Long]()
    var attributedWeightsMap = Map[String, Map[String, Double]]()

    convertedPaths.collect().foreach { row: Row => // For each converted user path

      channelFrequencyMap = row.getValuesMap(channelSet.toSeq)
      logger.info("A map of channels and their frequencies is " + channelFrequencyMap)

      var coefficientFrequencyMap = Map[String, Double]()

      //TODO:refactor below code into seperate method
      //-------------------------------------------------
      channelSet.foreach { key: String => // For each channel in the row
        // calculate the product of frequency and coefficient
        var coefficient = modellingResultMap.get(key).get
        if (coefficient < 0) {
          println("negative coeffiecients")
          //coefficient = 0
        }
        var frequency = channelFrequencyMap.get(key).get
        var value = coefficient * frequency

        coefficientFrequencyMap += (key -> value)
      }
      //------------------------------------------------------
      logger.info("A map of the product of frequency and coefficients for each channel is " + coefficientFrequencyMap)

      var channelConversionProbabilityMap = Map[String, Double]()
      var channelConversionWeightMap = Map[String, Double]()

      channelSet.foreach { channel: String => // For each channel calculate probability of conversion

        if (coefficientFrequencyMap.get(channel).get == 0) { //Edit : channelFrequencyMap changed to coefficientFrequencyMap
          channelConversionProbabilityMap += (channel -> 0) // This is done because to avoid divide by zero conditions
        } else {
          val channelConversionProbability =
            ( /*calculateOneChannelConversionProbability(channel, coefficientFrequencyMap) * calculateRestChannelsConversionProbability(channel, coefficientFrequencyMap)
                /
                calculateAllChannelConversionProbability(coefficientFrequencyMap)*/

              calculateAllChannelConversionProbability(coefficientFrequencyMap) - calculateRestChannelsConversionProbability(channel, coefficientFrequencyMap))

          channelConversionProbabilityMap += (channel -> channelConversionProbability)
        }

      }

      logger.info("************Granular level attribution************" + channelConversionProbabilityMap)

      channelConversionProbabilityMap.keySet.foreach { channel: String =>

        if (channelConversionProbabilityMap.values.sum == 0) {
          channelConversionWeightMap += (channel -> 0)
        } else {
          channelConversionWeightMap += (channel -> channelConversionProbabilityMap.get(channel).get / channelConversionProbabilityMap.values.sum)
        }

      }

      logger.info("**************Normalized weights***************" + channelConversionWeightMap)

      attributedWeightsMap += (row.get(0).toString() -> channelConversionWeightMap)

      //attributedWeightsMap

      def calculateAllChannelConversionProbability(coefficientFrequencyMap: Map[String, Double]): Double = {

        val intercept = modellingResultMap.get("Intercept").get
        val coefficientFrequencySum = coefficientFrequencyMap.values.sum
        val value = -(intercept + coefficientFrequencySum)

        val logisticTerm = (1 + Math.exp(value))
        val allChannelProbability = 1 / logisticTerm

        allChannelProbability

      }

      def calculateOneChannelConversionProbability(channelName: String, finalMap: Map[String, Double]): Double = {

        val intercept = modellingResultMap.get("Intercept").get
        val coefficientFrequencySum = finalMap.get(channelName).get
        val value = -(intercept + coefficientFrequencySum)

        val logisticTerm = (1 + Math.exp(value))
        val particularChannelProbability = 1 / logisticTerm

        particularChannelProbability

      }

      def calculateRestChannelsConversionProbability(channelName: String, coefficientFrequencyMap: Map[String, Double]): Double = {

        val intercept = modellingResultMap.get("Intercept").get
        val channelCoefficientFrequencySum = coefficientFrequencyMap.get(channelName).get
        val allCoefficientFrequencySum = coefficientFrequencyMap.values.sum
        val value = -(intercept + (allCoefficientFrequencySum - channelCoefficientFrequencySum))

        val logisticTerm = (1 + Math.exp(value))
        val restChannelsProbability = 1 / logisticTerm

        restChannelsProbability

      }

    }

    logger.info("This is it *******************" + attributedWeightsMap.values)

    attributedWeightsMap

  }

  def getOverallLevelAttribution(modellingResultMap: Map[String, Double]): Unit = {

    var channelSet = modellingResultMap.keySet
    channelSet -= "Intercept"

    var channelFrequencyMap = Map[String, Long]()
    var channelConversionProbabilityMap = Map[String, Double]()
    var channelConversionWeightMap = Map[String, Double]()

    channelSet.foreach { channel: String =>

      val channelConversionProbability = calculateChannelConversionProbability(channel, modellingResultMap)

      channelConversionProbabilityMap += (channel -> channelConversionProbability)

      println("*********************Granular level attribution" + channelConversionProbabilityMap)

      channelConversionProbabilityMap.keySet.foreach { channel: String =>
        channelConversionWeightMap += (channel -> channelConversionProbabilityMap.get(channel).get / channelConversionProbabilityMap.values.sum)
      }

      println("*********************Normalized weights" + channelConversionWeightMap)

    }

    def calculateChannelConversionProbability(channelName: String, finalMap: Map[String, Double]): Double = {

      val intercept = modellingResultMap.get("Intercept").get
      val coefficient = finalMap.get(channelName).get
      val value = -(intercept + coefficient)

      val logisticTerm = (1 + Math.exp(value))
      val eachChannelProbability = 1 / logisticTerm

      eachChannelProbability

    }

    channelConversionWeightMap

  }

}
