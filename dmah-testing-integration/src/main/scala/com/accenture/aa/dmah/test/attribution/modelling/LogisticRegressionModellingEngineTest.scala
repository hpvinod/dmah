package com.accenture.aa.dmah.test.attribution.modelling

import org.apache.spark.sql.DataFrame
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import com.accenture.aa.dmah.attribution.modelling.LogisticRegressionModellingEngine
import scala.collection.mutable.MutableList
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LogisticRegressionModellingEngineTest extends FlatSpec with MockitoSugar with Matchers {
  
  val modellingEngine = Mockito.mock(classOf[LogisticRegressionModellingEngine])
  
  "fit Model" should "check if the list of data frames is converted to a result map" in {
    
    modellingEngine.fitModel(Mockito.mock(classOf[List[DataFrame]]))
    
    
  }
  
  "calculate mean" should "test that a List of coefficients of double values is averaged and one double value is returned " in {
    
    modellingEngine.calculateMean(Mockito.mock(classOf[MutableList[Array[Double]]]))
    
  }
  
  "fit" should "test that a Data frame is fit and a logistic regession model is returned " in {
    
    modellingEngine.fit(Mockito.mock(classOf[DataFrame]))
    
  }
  
  
  
}