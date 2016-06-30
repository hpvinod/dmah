package com.accenture.aa.dmah.attribution.channelattribution.bo

class ROI extends Serializable {

  var channelName: String = null
  var ROI: Double = 0.0

  @Override
  override def toString(): String = {
    return channelName + "," + ROI.toString()
  }

}