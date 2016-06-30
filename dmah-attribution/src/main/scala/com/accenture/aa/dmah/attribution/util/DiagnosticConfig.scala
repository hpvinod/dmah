package com.accenture.aa.dmah.attribution.util

import scala.beans.BeanProperty

class DiagnosticConfig extends Serializable {

  @BeanProperty
  var attributionModeTest: Boolean = _

  @BeanProperty
  var hdfsFolder: String = _

  @BeanProperty
  var modelllingInputFile: String = _

  @BeanProperty
  var investmentInputFile: String = _

  @BeanProperty
  var contributionInputFile: String = _

  @BeanProperty
  var filteredNonConversionUserJrnyFile: String = _

  @BeanProperty
  var channelWtAttributionFile: String = _

  @BeanProperty
  var channelROIFile: String = _

  @BeanProperty
  var logisticsflattenedPath: String = _

  @BeanProperty
  var lrSampleTransformedData: String = _

  @BeanProperty
  var lrSampleCoefficients: String = _

  @BeanProperty
  var lrFinalCoefficients: String = _

  @BeanProperty
  var lrChannelROI: String = _

  @BeanProperty
  var lrUserChannelAttribution: String = _

  @BeanProperty
  var partitionNum: Integer = _

}