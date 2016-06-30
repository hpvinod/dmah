package com.accenture.aa.dmah.attribution.userjourney

trait IModellingUJTransformationFactory {

  def createInstance(technique: String): IModellingUJTransformation[Object]
}