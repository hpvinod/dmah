package com.accenture.aa.dmah.attribution.core

import scala.beans.BeanProperty

import org.apache.spark.sql.DataFrame
import org.slf4j.LoggerFactory

import com.accenture.aa.dmah.attribution.channelattribution.ChannelAttributionFactory
import com.accenture.aa.dmah.attribution.channelattribution.IChannelAttribution
import com.accenture.aa.dmah.attribution.channelattribution.bo.AttributionResults
import com.accenture.aa.dmah.attribution.core.bo.AttributionCommand
import com.accenture.aa.dmah.attribution.dataprocessing.IFetchAttributionInputs
import com.accenture.aa.dmah.attribution.modelling.IModellingEngine
import com.accenture.aa.dmah.attribution.userjourney.IUserJourneyTransformer
import org.apache.spark.sql.DataFrame

/**
 * @author vivek.gupt
 *
 */
class AttributionHub extends IAttributionHub {

  private val logger = LoggerFactory.getLogger(classOf[AttributionHub])

  @BeanProperty
  var fetchAttributionInputs: IFetchAttributionInputs = _
  
  @BeanProperty
  var userJourneyTransformer: IUserJourneyTransformer = _
  
  @BeanProperty
  var modellingEngine: IModellingEngine = _

  @BeanProperty
  var channelAttributionFactory: ChannelAttributionFactory = _

  /*@BeanProperty
  var userJourneyTransformationFactory: UserJourneyTransformationFactory = _

  @BeanProperty
  var modellingEngineFactory: ModellingEngineFactory = _*/

  def executeAttribution(attributionCommand: AttributionCommand): Unit = {

    /*
     * Fetch raw user journey
     */
    val rawUserJourney: DataFrame = fetchAttributionInputs.getInputsToAttribution()

    /*
     * Get transformed data for specified modeling technique
     * This data will be sorted on USer & Time
     */
   /* val userJourney: IUserJourneyTransformation[Object] = userJourneyTransformationFactory.createInstance(attributionCommand.modellingTechnique)
    val transformedUserJourney: Object = userJourney.getTransformedData(rawUserJourney)*/

    /*
     * Get transformed user journey data common to all modelling techniques
     */
    
    var transformedUserJourney:DataFrame = userJourneyTransformer.transformUserJourney(rawUserJourney)
    
    /*
     * Get results of modeling techniques
     * TODO: transformedUserJourney shd be passed
     */
    val modellingResults = modellingEngine.getModellingResults(transformedUserJourney, attributionCommand)
    
    /*
     * Fit model for specified modeling technique
     */
    /*val modellingEngineCore: ICoreModellingEngine = modellingEngineFactory.createInstance(attributionCommand.modellingTechnique)
    val modellingResultMap: Object = modellingEngineCore.fitModel(transformedUserJourney)*/
    
    /*
     * Compute attribution weights & ROI's for specified modeling technique
     */
    val channelAttribution: IChannelAttribution = channelAttributionFactory.createInstance(attributionCommand.modellingTechnique)
    val attributionWeights: AttributionResults = channelAttribution.getAttributionWeights(modellingResults)
    val attributionROI: AttributionResults = channelAttribution.getROI(attributionWeights)


  }

}
