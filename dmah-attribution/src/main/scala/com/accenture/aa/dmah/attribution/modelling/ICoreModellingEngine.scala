package com.accenture.aa.dmah.attribution.modelling

import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.spark.core.IQueryExecutor

trait ICoreModellingEngine {

  def fitModel(transformedData: Object): Object

}