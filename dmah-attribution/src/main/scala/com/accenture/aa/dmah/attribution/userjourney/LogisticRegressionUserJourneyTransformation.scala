package com.accenture.aa.dmah.attribution.userjourney

import scala.beans.BeanProperty

import scala.collection.mutable.ListBuffer
import com.accenture.aa.dmah.core.HdfsWriter
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types.DataTypes
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig
import com.accenture.aa.dmah.core.GlobalContext
import com.accenture.aa.dmah.spark.core.IQueryExecutor

/**
 * @author himanshu.babbar
 *
 * This class transforms the original user journey and passes the transformed data to modelling engine
 */
class LogisticRegressionUserJourneyTransformation[Object] extends AbstractModellingUJTransformation[Object] with Serializable {

  private val logger = LoggerFactory.getLogger(classOf[LogisticRegressionUserJourneyTransformation[Object]])
  var flattenedPath: DataFrame = null

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  @BeanProperty
  var sampleNumber: Int = _

  @BeanProperty
  var userJourneyTable: String = _

  @BeanProperty
  var diagnosticConfig: DiagnosticConfig = _

  /**
   * @author himanshu.babbar
   * @param DataFrame
   * @return List[DataFrame]
   *
   * This method takes the original user journey as the input and transforms it
   * to return a list of data frames which would serve as an input to
   * logistic regression modeling engine
   */
  override def getTransformedData(rawUserJourney: DataFrame): Object = {

    logger.info("In getTransformedData")
    // var rawUserJourneyDf:DataFrame= rawUserJourney
    flattenedPath = prepareFlattenedPathData(rawUserJourney)

    val dataFrameTuple = subsetConversionData(flattenedPath)

    val dataFrameCount = countDataFrameRows(dataFrameTuple)

    val sampleDataList = getSampleDataList(dataFrameCount, dataFrameTuple)

    val lRInput = prepareLRInput(sampleDataList)

    logger.info("Returning transformed data")

    lRInput.asInstanceOf[Object]

  }

  /**
   * @author himanshu.babbar
   * @param DataFrame
   * @return Tuple2[DataFrame, DataFrame]
   *
   * This method takes the flattened path data as the input and
   * subsets the data into conversion and non conversion data and
   * returns a tuple containing these two data frames
   *
   * TODO : Check if we can need to show sample data frame in debug mode
   */
  def subsetConversionData(flattenedPath: DataFrame): Tuple2[DataFrame, DataFrame] = {

    logger.info("Subsetting flattened path dataframe")

    //TODO: This code will not be useful, once multiple user journey feature will be implemented
    var updatedFlattenedPath = updateFlattenedPath(flattenedPath)

    var convertedPaths = updatedFlattenedPath.filter(updatedFlattenedPath("Conversion") === 1)
    var nonConvertedPaths = updatedFlattenedPath.filter(updatedFlattenedPath("Conversion") === 0)

    var dataFrameTuple = (convertedPaths, nonConvertedPaths)

    //TODO: This should be passed to another module through parameters
    GlobalContext.dataFrameTuple = dataFrameTuple

    logger.info("Returning converted and non converted data frames" + dataFrameTuple)

    dataFrameTuple
  }

  /**
   * @author himanshu.babbar
   * @param DataFrame
   * @return DataFrame
   *
   * This method updates the flattened path dataframe and
   * updates all the users having multiple conversions to 1
   * to make the target variable binary
   */
  def updateFlattenedPath(flattenedPath: DataFrame): DataFrame = {

    var updateConversions = udf { (Conversion: Int) =>
      if (Conversion > 1) 1 else Conversion
    }

    var updatedFlattenedPath = flattenedPath.withColumn("Conversion", updateConversions(flattenedPath("Conversion")))

    updatedFlattenedPath
  }

  /**
   * @author himanshu.babbar
   *
   * This method counts the number of rows of the
   * converted and non converted rows data frames and
   * returns a tuple containing both these counts
   */
  def countDataFrameRows(dataFrameTuple: Tuple2[DataFrame, DataFrame]): Tuple2[Long, Long] = {

    var conversionCount: Long = dataFrameTuple._1.count()
    var nonConversionCount: Long = dataFrameTuple._2.count()

    var dataFrameCount = (conversionCount, nonConversionCount)

    logger.info("Number of rows in converted data frame = " + conversionCount)
    logger.info("Number of rows in non-converted data frame = " + nonConversionCount)

    dataFrameCount
  }

  //TODO: No. of sample, sample size & limit on sample size should read from configuration
  /**
   * @author himanshu.babbar
   * @param Tuple2[Long, Long], Tuple2[DataFrame, DataFrame]
   * @return List[DataFrame]
   *
   * This method iterates over a configurable number of iterations
   * and returns a list of data frames which would serve as an input to mlib logistic regression
   *
   * For each iteration or bagging, we sample the converted and non-converted data frames in the
   * proportion of their counts and return a union of these two sampled dataframes.
   *
   * We transform each sample dataframe to a logistic specific input
   */
  def getSampleDataList(dataFrameCount: Tuple2[Long, Long], dataFrameTuple: Tuple2[DataFrame, DataFrame]): List[DataFrame] = {

    logger.info("Sampling the data frame in getSampleDataList")

    var sampledDataFrameListBuffer = ListBuffer[DataFrame]()

    for (x: Int <- 1 to sampleNumber) {
      var sampledDataFrame: DataFrame = null

      var conversionSample = dataFrameTuple._1.sample(true, (0.15D)).limit((dataFrameCount._1 * 0.1).toInt) // 0.15 represents the approximate fraction (15%) of dataframe that would be returned after sampling

      var nonConversionSample = dataFrameTuple._2.sample(true, (0.15D)).limit((dataFrameCount._2 * 0.1).toInt) // Using limit ensures only a fixed number of the rows are returned 

      sampledDataFrame = conversionSample.unionAll(nonConversionSample)
      HdfsWriter.writeDFToHdfs(flattenedPath, "sampleDataFrame" + x, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum)
      sampledDataFrameListBuffer += (sampledDataFrame)
    }

    logger.info("Returning a list of sampled data frames")

    sampledDataFrameListBuffer.toList

  }

  /**
   * @author himanshu.babbar
   * @param DataFrame
   * @return DataFrame
   *
   * This method takes the sample data frame and converts it into logistic regression specific input data
   *
   * It uses Vector assembler to create a 'features' column using all the feature variables in the data frame and
   * rename the target variable (dependent variable) as 'label'
   *
   * It is imperative we follow the naming convention for mlib logistic regression
   */
  def prepareLRInput(sampleDataFrameList: List[DataFrame]): List[DataFrame] = {

    logger.info("Preparing logistic regression input in prepareLRInput")

    //TODO: This should read from configuration
    var featureColumns = flattenedPath.columns.diff(Array("userid", "Conversion"))

    //TODO: featureColumns should pass to modelling component as a parameter
    GlobalContext.featureColumns = featureColumns

    val assembler = new VectorAssembler()
      .setInputCols(featureColumns)
      .setOutputCol("features")

    var lRInputDFListBuffer = ListBuffer[DataFrame]()

    for (i: Int <- 0 to sampleDataFrameList.length - 1) {
      val output = assembler.transform(sampleDataFrameList(i))

      val lRInputData = output.withColumn("label", output.col("Conversion").cast(DataTypes.DoubleType))
        .select("label", "features")

      logger.info("Returning LR specific input with label and features")

      lRInputDFListBuffer += lRInputData
    }

    lRInputDFListBuffer.toList

  }

  //TODO : Check if we can club the sql queries
  /**
   * @author himanshu.babbar
   * @param DataFrame
   * @return DataFrame
   *
   * This method reads the raw user journey and converts it into a flattened path structure
   */
  def prepareFlattenedPathData(rawUserJourney: DataFrame): DataFrame = {

    this.queryExecutor.executeQuery("Drop table if exists groupedChannels")

    val groupedChannelQuery = "create table groupedChannels as select userid, channel, count(channel) as originalFrequency from " + "userJourneyLogistics" + " group by userid, channel having count(channel) > 0 order by userid, channel"
    this.queryExecutor.executeQuery(groupedChannelQuery)
    this.queryExecutor.executeQuery("Drop table if exists userJourneyLogistics")
    
    this.queryExecutor.executeQuery("Drop table if exists distinctChannels")
    val distinctChannelQuery = "create table distinctChannels as Select distinct channel from " + userJourneyTable + " order by channel"
    this.queryExecutor.executeQuery(distinctChannelQuery)

    var groupedData = this.queryExecutor.executeQuery(""" With tmp as 
                                           (Select distinct g.userid, d.channel from groupedChannels g join distinctChannels d)
                                           Select tmp.userid, tmp.channel, coalesce(g.originalFrequency, 0) as frequency
                                           from tmp left outer join groupedChannels g
                                           on tmp.userid = g.userid and tmp.channel = g.channel
                                           order by userid asc, channel asc """)

    groupedData.show()

    var flattenedPath = groupedData
      .groupBy("userid")
      .pivot("channel")
      .sum("frequency")

    this.queryExecutor.executeQuery("Drop table if exists FP2")
    flattenedPath.saveAsTable("FP2")

    //     var flattenedPath = this.queryExecutor.executeQuery("Select * from FP2")
    flattenedPath.show()
    HdfsWriter.writeDFToHdfs(flattenedPath, diagnosticConfig.logisticsflattenedPath, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum)
    flattenedPath
  }
}
