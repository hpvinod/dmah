package com.accenture.aa.dmah.attribution.modelling

import org.apache.log4j.LogManager
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourneyTransformedData
import com.accenture.aa.dmah.attribution.modelling.bo.ModellingResults
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.core.GlobalContext
import scala.beans.BeanProperty
import org.apache.spark.sql.DataFrame
import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import org.apache.spark.sql.Row
import java.util.Date

import scala.collection.mutable.ListBuffer
import java.text.SimpleDateFormat
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.spark.core.IQueryExecutor

class HeuristicsModellingEngine extends AbstractCoreModellingEngine with Serializable {

  private val logger = LoggerFactory.getLogger(classOf[HeuristicsModellingEngine])

  @BeanProperty
  var heuristicPositionBased: HeuristicPositionBased = _

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  override def fitModel(transformedData: Object): Object = {
    transformedData
  }

}