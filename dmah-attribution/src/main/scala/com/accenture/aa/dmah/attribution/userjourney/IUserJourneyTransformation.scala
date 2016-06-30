package com.accenture.aa.dmah.attribution.userjourney

import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourneyTransformedData
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.IQueryExecutor

trait IUserJourneyTransformation[Object] {

  def getTransformedData(rawUserJourney: DataFrame): Object
}