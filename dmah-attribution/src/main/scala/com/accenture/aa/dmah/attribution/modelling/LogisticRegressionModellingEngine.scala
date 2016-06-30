package com.accenture.aa.dmah.attribution.modelling

import org.slf4j.LoggerFactory
import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.modelling.bo.ModellingResults
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourneyTransformedData
import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.linalg.{ Vector, Vectors, VectorUDT }
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.stat.test.ChiSqTestResult
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.DenseVector
import scala.collection.mutable.MutableList
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL
import com.accenture.aa.dmah.core.GlobalContext
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import com.accenture.aa.dmah.core.HdfsWriter
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig

/**
 * @author himanshu.babbar
 *
 * This class implements the modeling for logistic regression using mlib
 */
class LogisticRegressionModellingEngine extends AbstractCoreModellingEngine with Serializable {

  private val logger = LoggerFactory.getLogger(classOf[LogisticRegressionModellingEngine])

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  @BeanProperty
  var diagnosticConfig: DiagnosticConfig = _

  /**
   * @author himanshu.babbar
   * @param List[DataFrame]
   * @return Map[String, Double]
   *
   * This method implements the abstract method fitModel for Logistic Regression
   *
   * This takes as input a list of data frames of logistic regression specific input
   * and returns a map of all features and intercepts and their coefficients
   *
   */
  override def fitModel(transformedData: Object): Object = {

    logger.info("Fitting Logistic regression model on a list of data frames")

    var coefficentsList = new MutableList[Array[Double]]
    var logisticRegressionCoefficientMap = Map[String, Double]()

    var featureColumns = GlobalContext.featureColumns
    var interceptAndFeatures = "Intercept" +: featureColumns
    var length = (transformedData.asInstanceOf[List[DataFrame]]).length
    var ctr = 0
    transformedData.asInstanceOf[List[DataFrame]].foreach(
      (logisticRegressionInputDF: DataFrame) => {
        ctr = ctr + 1
        //	    HdfsWriter.writeDFToHdfs(logisticRegressionInputDF, diagnosticConfig.lrSampleTransformedData+ctr, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum)
        var lRModel = fit(logisticRegressionInputDF)
        var coefficientArray = lRModel.coefficients.toArray
        var intercept = lRModel.intercept
        var lrSampleCoefficientMap = Map[String, Double]()

        var coefficients = intercept +: coefficientArray

        for (i: Int <- 0 to interceptAndFeatures.length - 1) {
          lrSampleCoefficientMap += (interceptAndFeatures(i) -> coefficients(i))
        }
        coefficentsList += coefficients
        HdfsWriter.writeMapToHdfs(lrSampleCoefficientMap, diagnosticConfig.lrSampleCoefficients + ctr, diagnosticConfig.hdfsFolder)
      })

    var finalCoefficients: Array[Double] = calculateMean(coefficentsList)

    for (i: Int <- 0 to interceptAndFeatures.length - 1) {
      logisticRegressionCoefficientMap += (interceptAndFeatures(i) -> finalCoefficients(i))
    }
    HdfsWriter.writeMapToHdfs(logisticRegressionCoefficientMap, diagnosticConfig.lrFinalCoefficients, diagnosticConfig.hdfsFolder)
    logger.info("Returning a map of intercept and features and their coefficents")

    logisticRegressionCoefficientMap

  }

  /**
   * @author himanshu.babbar
   * @param MutableList[Array[Double]]
   * @return Array[Double]
   *
   * This method calculates the mean of coefficients over the total number of data frames
   * as per the number of iterations decided
   *
   * TODO : check the values after reduceLeft and map functions and update comments
   */
  def calculateMean(coefficientsList: MutableList[Array[Double]]): Array[Double] = {

    logger.info("Calculating mean of coefficients and intercepts")

    var sumCoefficients = coefficientsList.reduceLeft { (acc, vec) =>
      acc zip vec map {
        case (a, b) => a + b
      }
    }

    var meanCoefficients = sumCoefficients.map { (coefficient: Double) => coefficient / coefficientsList.size }

    logger.info("Returning an array of values of the mean of coefficients of each channel and intercept")

    meanCoefficients
  }

  /**
   * @author himanshu.babbar
   * @param DataFrame
   * @return LogisticRegressionModel
   *
   * This method fits a logistic regression model on a LR input dataframe
   * and return a LogisticRegressionModel object
   *
   * We can also set various parameters for the model here
   *
   * TODO : Check for this property, whether it should be removed or not
   */
  def fit(logisticRegressionInputDF: DataFrame): LogisticRegressionModel = {

    logger.info("Fitting logistic regression model on a data frame")

    var logisticRegression = new LogisticRegression()

    //logisticRegression.setMaxIter(10)

    var lRModel = logisticRegression.fit(logisticRegressionInputDF)

    logger.info("Returning an object of LogisticRegressionModel()")

    lRModel

  }

  def test(logisticRegressionInputDF: DataFrame): Array[Double] = {

    var logisticRegressionRDD = logisticRegressionInputDF.map(row => LabeledPoint(row.getDouble(0), row(1).asInstanceOf[Vector]))
    var pValuesList = new MutableList[Double]

    var lRTestResults: Array[ChiSqTestResult] = Statistics.chiSqTest(logisticRegressionRDD)
    lRTestResults.foreach { result =>
      pValuesList += result.pValue
    }

    pValuesList.toArray
  }

}