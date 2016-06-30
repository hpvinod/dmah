package com.accenture.aa.dmah.core


trait IJobRunner {
  var propertiesFileName:String;
  def createObject: DMAHJob
}