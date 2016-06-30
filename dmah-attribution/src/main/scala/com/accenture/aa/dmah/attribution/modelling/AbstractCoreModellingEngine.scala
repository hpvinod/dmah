package com.accenture.aa.dmah.attribution.modelling

import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.IQueryExecutor

abstract class AbstractCoreModellingEngine extends ICoreModellingEngine {

  def fitModel(transformedData: Object): Object

}