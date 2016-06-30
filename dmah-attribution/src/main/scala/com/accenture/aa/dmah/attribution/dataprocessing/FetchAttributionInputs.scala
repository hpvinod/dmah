package com.accenture.aa.dmah.attribution.dataprocessing

import scala.beans.BeanProperty

import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.core.GlobalContext
import com.accenture.aa.dmah.spark.core.QueryExecutor
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.SparkContainer
import com.accenture.aa.dmah.core.HdfsWriter
import com.accenture.aa.dmah.attribution.util.DiagnosticConfig
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{lag, sum}
import com.accenture.aa.dmah.attribution.util.AttributionTechnique

class FetchAttributionInputs extends IFetchAttributionInputs {

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  @BeanProperty
  var sparkContainer: SparkContainer = _

  @BeanProperty
  var userJourneyData: String = _

  @BeanProperty
  var salesData: String = _

  @BeanProperty
  var investmentData: String = _

  @BeanProperty
  var diagnosticConfig: DiagnosticConfig = _

  def getInputsToAttribution(): DataFrame = {
    fetchAttributionData()
  }

  def fetchAttributionData(): DataFrame = {

    val startDate = GlobalContext.mappingProperties.get("attribution").get.getProperty("startDate").toString()
    val endDate = GlobalContext.mappingProperties.get("attribution").get.getProperty("endDate").toString()
    
    val query = "Select * from " + userJourneyData + " where to_date(time) between ('" + startDate + "') and ('" + endDate + "')" 
    var rawUserJourney = this.queryExecutor.executeQuery(query)
    
    //Code to append subpath
    val window = Window.partitionBy("userid").orderBy("time")
		val change = ((lag("channel", 1).over(window) <=> "Conversion")).cast("int")
		val subpath = sum(change).over(window)

		var userJourneyWithSubpath = rawUserJourney.withColumn("subpathId", subpath)
		userJourneyWithSubpath.show()
    
		//Code to transform userid key
    var userJourneyDf = userJourneyWithSubpath.selectExpr("CONCAT_WS('_', userid, subpathId) as userid", "time", "channel", "subpathId")
                                              .orderBy("userid", "time")
    

    println("Sorted data frame ............." + userJourneyDf.show())

    GlobalContext.userJourney = userJourneyDf

    var sales = this.queryExecutor.executeQuery("Select * from " + salesData)
    sales.show()
    GlobalContext.salesData = sales

    //	  HdfsWriter.writeToHdfs(sales, "sales.csv", "/tmp/", 1)

    var investment = this.queryExecutor.executeQuery("Select * from " + investmentData)
    investment.show()
    GlobalContext.investment = investment
    if (diagnosticConfig.attributionModeTest) {
      HdfsWriter.writeDFToHdfs(userJourneyDf, diagnosticConfig.modelllingInputFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum)
      HdfsWriter.writeDFToHdfs(sales, diagnosticConfig.contributionInputFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum)
      HdfsWriter.writeDFToHdfs(investment, diagnosticConfig.investmentInputFile, diagnosticConfig.hdfsFolder, diagnosticConfig.partitionNum)
    }
    
    if(GlobalContext.mappingProperties.get("attribution").get.getProperty("modellingTechnique").equalsIgnoreCase(AttributionTechnique.LOGISTICS)){
      this.queryExecutor.executeQuery("Drop table if exists userJourneyLogistics")
      userJourneyDf.saveAsTable("userJourneyLogistics")
    }
  
    userJourneyDf
    
  }

}