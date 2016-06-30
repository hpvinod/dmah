package com.accenture.aa.dmah.attribution.userjourney

import org.apache.log4j.LogManager
import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourneyTransformedData
import org.slf4j.LoggerFactory
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.IQueryExecutor

class MarkovUserJourneyTransformation[Object] extends AbstractModellingUJTransformation[Object] {

  private val logger = LoggerFactory.getLogger(classOf[MarkovUserJourneyTransformation[Object]])

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  override def getTransformedData(rawUserJourney: DataFrame): Object = {
    rawUserJourney.asInstanceOf[Object]
  }
}