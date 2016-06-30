package com.accenture.aa.dmah.attribution.channelattribution.bo

class ChannelWeightsAtGranularLevel extends Serializable {

  var channelName: String = null
  var channelWeight: Double = 0.0
  var attributedSales: Double = 0.0
  var userID: String = null

  @Override
  override def toString(): String = {
    return userID + "," + channelName + "," + channelWeight.toString() + "," + attributedSales.toString()
  }
}