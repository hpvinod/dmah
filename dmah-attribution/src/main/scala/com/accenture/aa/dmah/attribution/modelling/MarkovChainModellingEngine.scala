package com.accenture.aa.dmah.attribution.modelling

import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import scala.beans.BeanProperty

class MarkovChainModellingEngine extends AbstractCoreModellingEngine {

  private val logger = LoggerFactory.getLogger(classOf[MarkovChainModellingEngine])

  @BeanProperty
  var queryExecutor: IQueryExecutor = _

  override def fitModel(transformedData: Object): Object = {
    null
  }
}