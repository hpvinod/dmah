package com.accenture.aa.dmah.attribution.modelling

import scala.beans.BeanProperty

import com.accenture.aa.dmah.attribution.core.bo.AttributionCommand
import com.accenture.aa.dmah.attribution.userjourney.IModellingUJTransformation
import com.accenture.aa.dmah.attribution.userjourney.ModellingUJTransformationFactory
import org.apache.spark.sql.DataFrame

class ModellingEngine extends IModellingEngine {

  @BeanProperty
  var modellingUJTransformationFactory: ModellingUJTransformationFactory = _
  
  @BeanProperty
  var coreModellingEngineFactory: CoreModellingEngineFactory = _

  def getModellingResults(transformedUserJourney: DataFrame, attributionCommand:AttributionCommand): Object = {

    /*
     * Get transformed data for specified modeling technique
     * This data will be sorted on USer & Time
     */
    val userJourney: IModellingUJTransformation[Object] = modellingUJTransformationFactory.createInstance(attributionCommand.modellingTechnique)
    val modellingUserJourneyTransformation: Object = userJourney.getTransformedData(transformedUserJourney)
    
    /*
     * Fit model for specified modeling technique
     */
    val modellingEngineCore: ICoreModellingEngine = coreModellingEngineFactory.createInstance(attributionCommand.modellingTechnique)
    val modellingResultMap: Object = modellingEngineCore.fitModel(modellingUserJourneyTransformation)

    modellingResultMap
  }
}