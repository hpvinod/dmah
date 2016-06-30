package com.accenture.aa.dmah.attribution.userjourney

import com.accenture.aa.dmah.attribution.util.AttributionTechnique
import com.accenture.aa.dmah.spark.core.IQueryExecutor
import scala.beans.BeanProperty

/**
 * @author vivek.gupt
 *
 */
class ModellingUJTransformationFactory extends IModellingUJTransformationFactory {

  @BeanProperty
  var heuristicsUserJourneyTransformation: HeuristicsUserJourneyTransformation[Object] = _

  @BeanProperty
  var logisticRegressionUserJourneyTransformation: LogisticRegressionUserJourneyTransformation[Object] = _

  @BeanProperty
  var markovUserJourneyTransformation: MarkovUserJourneyTransformation[Object] = _

  @BeanProperty
  var randomForestUserJourneyTransformation: RandomForestUserJourneyTransformation[Object] = _

  def createInstance(technique: String): IModellingUJTransformation[Object] = {

    var userJourneyTransformation: IModellingUJTransformation[Object] = null

    val tech = technique match {

      case AttributionTechnique.LOGISTICS =>
        userJourneyTransformation = logisticRegressionUserJourneyTransformation

      case AttributionTechnique.HEURISTICS =>
        userJourneyTransformation = heuristicsUserJourneyTransformation

      case AttributionTechnique.MARKOV =>
        userJourneyTransformation = markovUserJourneyTransformation

      case AttributionTechnique.RANDOM_FOREST =>
        userJourneyTransformation = randomForestUserJourneyTransformation
    }

    userJourneyTransformation

  }

}

