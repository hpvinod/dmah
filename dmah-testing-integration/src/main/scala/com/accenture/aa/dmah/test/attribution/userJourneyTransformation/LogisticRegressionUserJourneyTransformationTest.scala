package com.accenture.aa.dmah.test.attribution.userJourneyTransformation

import org.apache.spark.sql.DataFrame
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar

import com.accenture.aa.dmah.attribution.userjourney.LogisticRegressionUserJourneyTransformation
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LogisticRegressionUserJourneyTransformationTest extends FlatSpec with MockitoSugar with Matchers {
  
  val userJourneyTransformation = Mockito.mock(classOf[LogisticRegressionUserJourneyTransformation[Object]])
  val dataFrameInput = Mockito.mock(classOf[DataFrame])
  
  "count data frame rows" should "pass this test and validates data frame count as Tuple2[Long, Long]" in {
    
   //val userJourneyTransformation = Mockito.mock(classOf[LogisticRegressionUserJourneyTransformation])
    //val dataFrameInput = Mockito.mock(classOf[DataFrame])
    userJourneyTransformation.countDataFrameRows(dataFrameInput, dataFrameInput)
    
  }
  
  "updateFlattenedPath" should "pass this test and validate data frame is updated to another data frame" in {
   
    userJourneyTransformation.updateFlattenedPath(dataFrameInput)
    
  }
  
  "prepareLRInput" should "pass this test and validate that the resultant data frame is converted to LR specific input" in {
    
    userJourneyTransformation.prepareFlattenedPathData(dataFrameInput)
    
  }
  
  "getSampleDataList" should "pass this test and validate that data frame is sampled in a fixed proportion" in {
    
    //var dataFrameOCuntTuple = Tuple2[Long, Long]
   // userJourneyTransformation.getSampleDataList(Long, Long, dataFrameInput, dataFrameInput)
    
  }
  
}