package com.accenture.aa.dmah.attribution.channelattribution.bo

class SalesData extends Serializable {

  var userId: String = null
  var timeStamp: Long = 0L
  var salesValue: Double = 0.0

  @Override
  override def toString(): String = {
    return userId + "," + timeStamp.toString() + "," + salesValue.toString()
  }

}