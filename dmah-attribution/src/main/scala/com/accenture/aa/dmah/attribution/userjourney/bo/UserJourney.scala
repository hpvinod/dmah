package com.accenture.aa.dmah.attribution.userjourney.bo

class UserJourney extends Serializable {

  var userId: String = null
  var timeStamp: Long = 0L
  var channelName: String = null
  var sales: Double = 0.0

  @Override
  override def toString(): String = {
    return userId + "," + timeStamp.toString() + "," + channelName + "," + sales.toString()
  }

}