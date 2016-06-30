package com.accenture.aa.dmah.test.attribution.channelattribution

import org.mockito.Mockito
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar

import com.accenture.aa.dmah.attribution.channelattribution.LogisticChannelAttribution
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LRChannelAttributionTest extends FlatSpec with MockitoSugar with Matchers {
  
  val channelAttribution = Mockito.mock(classOf[LogisticChannelAttribution])
  
  "get attribution weights" should "test that a modelling result map is transformed into attribution weights" in {
    
    channelAttribution.getAttributionWeights(Mockito.mock(classOf[Map[String, Double]]))
    
  }
  
  "get granular level ROI" should "test that granular level attribution weight map is converted into ROI's" in {
    
    //channelAttribution.getGranularLevelROI(Mockito.mock(classOf[Map[String,Map[String, Double]]]))
    
  }
  
 /* "" should "" in {
    
  }*/
  
}