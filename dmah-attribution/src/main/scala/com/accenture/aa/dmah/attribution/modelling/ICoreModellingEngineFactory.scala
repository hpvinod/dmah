package com.accenture.aa.dmah.attribution.modelling

trait ICoreModellingEngineFactory {
  def createInstance(technique: String): ICoreModellingEngine
}