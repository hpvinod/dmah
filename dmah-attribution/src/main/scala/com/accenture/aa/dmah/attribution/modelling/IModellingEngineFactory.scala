package com.accenture.aa.dmah.attribution.modelling

trait IModellingEngineFactory {
  def createInstance(technique: String): ICoreModellingEngine
}