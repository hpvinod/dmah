package com.accenture.aa.dmah.attribution.modelling

import com.accenture.aa.dmah.attribution.util.AttributionTechnique
import scala.beans.BeanProperty

class CoreModellingEngineFactory extends ICoreModellingEngineFactory {

  @BeanProperty
  var logisticRegressionModellingEngine: LogisticRegressionModellingEngine = _

  @BeanProperty
  var heuristicsModellingEngine: HeuristicsModellingEngine = _

  @BeanProperty
  var markovChainModellingEngine: MarkovChainModellingEngine = _

  @BeanProperty
  var randomForestModellingEngine: RandomForestModellingEngine = _

  def createInstance(technique: String): ICoreModellingEngine = {

    var modellingEngine: ICoreModellingEngine = null

    val tech = technique match {

      case AttributionTechnique.LOGISTICS =>
        modellingEngine = logisticRegressionModellingEngine

      case AttributionTechnique.HEURISTICS =>
        modellingEngine = heuristicsModellingEngine

      case AttributionTechnique.MARKOV =>
        modellingEngine = markovChainModellingEngine

      case AttributionTechnique.RANDOM_FOREST =>
        modellingEngine = randomForestModellingEngine
    }

    modellingEngine
  }
}