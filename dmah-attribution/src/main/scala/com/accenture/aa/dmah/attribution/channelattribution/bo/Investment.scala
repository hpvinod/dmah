package com.accenture.aa.dmah.attribution.channelattribution.bo

class Investment extends Serializable {

  var channelName: String = null
  var investment: Long = 0

  @Override
  override def toString(): String = {
    return channelName + "," + investment.toString()
  }
}