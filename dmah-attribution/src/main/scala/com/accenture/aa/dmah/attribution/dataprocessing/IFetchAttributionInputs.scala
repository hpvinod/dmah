package com.accenture.aa.dmah.attribution.dataprocessing

import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import com.accenture.aa.dmah.spark.core.SparkContainer

trait IFetchAttributionInputs {

  def getInputsToAttribution(): DataFrame

}