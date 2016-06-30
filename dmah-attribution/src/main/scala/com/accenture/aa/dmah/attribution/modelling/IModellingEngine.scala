package com.accenture.aa.dmah.attribution.modelling

import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.attribution.core.bo.AttributionCommand

trait IModellingEngine {

  def getModellingResults(transformedUserJourney: DataFrame, attributionCommand:AttributionCommand): Object
}