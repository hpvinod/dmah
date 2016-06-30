package com.accenture.aa.dmah.attribution.channelattribution

import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.util.AttributionTechnique

class HeuristicTechniqueFactory extends Serializable {

  @BeanProperty
  var heuristicPositionBased: HeuristicChannelAttributionPositionBased = _

  @BeanProperty
  var heuristicLastClick: HeuristicChannelAttributionLastClick = _

  def createInstance(technique: String): IHeuristicTechnique = {

    var heuristicTechnique: IHeuristicTechnique = null

    val tech = technique match {

      case AttributionTechnique.LASTCLICK =>
        heuristicTechnique = heuristicLastClick

      case AttributionTechnique.POSITIONBASED =>
        heuristicTechnique = heuristicPositionBased

    }
    heuristicTechnique

  }

}

