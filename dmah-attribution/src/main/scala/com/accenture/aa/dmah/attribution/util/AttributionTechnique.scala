package com.accenture.aa.dmah.attribution.util

object AttributionTechnique extends Enumeration {

  //type Technique = Value
  val LOGISTICS = "Logistics"
  val HEURISTICS = "Heuristics"
  val RANDOM_FOREST = "RandomForest"
  val MARKOV = "Markov"
  val LASTCLICK = "LastClick"
  val POSITIONBASED = "PositionBased"
}