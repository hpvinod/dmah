package com.accenture.aa.dmah.attribution.channelattribution.bo

import scala.beans.BeanProperty

class HeuristicsWeightsAssignment extends Serializable {

  @BeanProperty
  var originatorWeight = 0d

  @BeanProperty
  var convertorWeight = 0d

  @BeanProperty
  var intermediateWeight = 0d

}