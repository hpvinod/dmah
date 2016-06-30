package com.accenture.aa.dmah.test.attribution.channelattribution

import org.mockito.Mockito
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionLastClick
import com.accenture.aa.dmah.attribution.channelattribution.LogisticChannelAttribution
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionPositionBased
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import org.mockito.Matchers.{ eq => mockEq }
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.prop.Whenever

import org.slf4j.Logger
import org.mockito.Mockito
import org.mockito.Mockito.verify

import org.specs2.Specification
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach

import scala.collection.mutable.ListBuffer
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionLastClick
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionPositionBased
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData

import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionLastClick
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionPositionBased
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData

import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionLastClick
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionPositionBased
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData

import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionLastClick
import com.accenture.aa.dmah.attribution.userjourney.bo.UserJourney
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicChannelAttributionPositionBased
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.bo.SalesData
import com.accenture.aa.dmah.attribution.channelattribution.bo.HeuristicsWeightsAssignment
 

@RunWith(classOf[JUnitRunner])
class LastClickHeuristicRunnerNew   extends FlatSpec with MockitoSugar with Matchers {
  
  val channelAttribution = Mockito.mock(classOf[LogisticChannelAttribution])
  
  "get attribution weights" should "test that a modelling result map is transformed into attribution weights" in {
    
    channelAttribution.getAttributionWeights(Mockito.mock(classOf[Map[String, Double]]))
    
  }
  
  "get granular level ROI" should "test that granular level attribution weight map is converted into ROI's" in {
    
    //channelAttribution.getGranularLevelROI(Mockito.mock(classOf[Map[String,Map[String, Double]]]))
    
  }
  /*"ChannelweightatGranularlevel" should "pass this test, because it checks if object is created or not" in {
         
         var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="VT_Display"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "123"
         
         
         var HeuristicObj = new HeuristicChannelAttributionLastClick()
         //testbootstrap.isValidInput()
         var channelWt : ChannelWeightsAtGranularLevel = null
         var channelWtobj = HeuristicObj.singleChannelWeightAssignment("123",userJourneyObj, 0.8, 10) 
         assert( channelWtobj.isInstanceOf[ChannelWeightsAtGranularLevel])
       }
      */
      /* 
       * The test case checks if addChannelWeightAcrossAllUsers method returns the ChannelWeightsAtGranularlevel object by summing 
       * up the weights of the two ChannelWeightsAtGranularlevel objects as input
       * */
      /*"addChannelWeightAcrossAllUsers" should "pass this test, because it checks for sum of the weights of 2 different ChannelWeightsAtGranularlevel" in {
        
         var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="VT_Display"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "123"
         
         
         var HeuristicObj = new HeuristicChannelAttributionLastClick()
         //testbootstrap.isValidInput()
         var channelWt : ChannelWeightsAtGranularLevel = null
         var channelWtB : ChannelWeightsAtGranularLevel = new ChannelWeightsAtGranularLevel()
         var channelWtA : ChannelWeightsAtGranularLevel = new ChannelWeightsAtGranularLevel
         
         channelWtB.channelName = "ctDispaly"
         channelWtB.channelWeight = 200
         channelWtB.attributedSales = 1000
         channelWtB.userID = "123"
         
         channelWtA.channelName = "VtDisplay"
         channelWtA.channelWeight = 100
         channelWtA.attributedSales = 10000
         channelWtA.userID = "1234"
         channelWt = HeuristicObj.addChannelWeightAcrossAllUsers(channelWtA, channelWtB)
         assert(channelWt.channelWeight == 300) 
       }
      
      
       * The test case passes the iterable of UserJourney objects that does not contains conversion and we check
       * if the method returns false
       * 
      
      "TestCase" should "pass this test, because it removes nonconverting row out " in {
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="VT_Display"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="VT_Display"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "123"
            store = userJourneyObj2 +: store
           assert(heuristicObj.filterOutnonConversion(userJourneyObj.userId, store.toIterable)=== false)
     }
      */
      
      /*
       * The test case passes the iterable of UserJourney objects that contains conversion and we check
       * if the method returns true
       * */
      
      "TestCase" should "pass this test, because it accepts converting row " in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="VT_Display"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="conversion"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "123"
            store = userJourneyObj2 +: store
           assert(heuristicObj.filterOutnonConversion(userJourneyObj.userId, store.toIterable)=== true)
      
      
      
      
        
      }
      
        "TestCase" should "pass as the method return a List obj" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="VT_Display"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="CT_Display"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "123"
            store = userJourneyObj2 +: store
            var salesObj = new SalesData()
           salesObj.userId = "123"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
            var listObj = (heuristicObj.channelWeightsCalculationLastClick("123", store.toIterable, salesObj))
           assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularLevel]])
      
      
      
      
        
      }
      
      "TestCase" should "pass as the method PositionBased  return a List obj" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionPositionBased()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="VT_Display"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="CT_Display"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "123"
            store = userJourneyObj2 +: store
            var salesObj = new SalesData()
           salesObj.userId = "123"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
            var listObj = (heuristicObj.positionBasedChannelWeightsCalc("123", store.toIterable, salesObj ,weightAssignmentObj ) )
           assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularLevel]])      
        
      }
      
      /*
       * The test case will check delegateHeuristicsType method which is configured to run in PB mode and it returns
       * a list of  ChannelWeightsAtGranularlevel objs whose sum of weights is equal to 1
       * */
       "TestCase" should "pass as the method delegateHeuristicType return a List of UserJourney Obj the weights of the userJourney Obj sums up to 1" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="facebook"
         userJourneyObj.sales = 160
         userJourneyObj.userId = "1205"
         userJourneyObj.timeStamp = 10
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="vtDisplay"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "1205"
         userJourneyObj2.timeStamp = 20
            store = userJourneyObj2 +: store
            
            var userJourneyObj3= new UserJourney()
         userJourneyObj3.channelName="conversion"
         userJourneyObj3.sales = 120.0
         userJourneyObj3.userId = "1205"
         userJourneyObj3.timeStamp = 30
            store = userJourneyObj3 +: store
            
            
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 5
           salesObj.salesValue = 1000
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            var listObj =  (heuristicObj.callLastClick("1205", store.toIterable, salesObj, weightAssignmentObj) )
           
            var sum : Double =0.0
           /* var granularObj = listObj(0)
            sum = sum + granularObj.channelWeight*/
            
            for(ctr<- 0 to listObj.length -1){
              
              var tempSum : Double = listObj(ctr).channelWeight
              
              sum = sum+tempSum
            }
            assert(sum == 1) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
       /*
       * The test case will check delegateHeuristicsType method which is configured to run in PB mode and it returns
       * a list of  ChannelWeightsAtGranularlevel objs whose sum of weights is equal to 1. We paass a list of size 4 as input including
       * conversion
       * */
       
         "TestCase" should "pass as the method delegateHeuristicType return a List obj with  1 channel weights for each row" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="facebook"
         userJourneyObj.timeStamp = 10
         userJourneyObj.sales = 160
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="vtDisplay"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "1205"
         userJourneyObj2.timeStamp = 20
            store = userJourneyObj2 +: store
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
           
             var userJourneyObj3= new UserJourney()
         userJourneyObj3.channelName="GoogleBrand"
         userJourneyObj3.sales = 120.0
         userJourneyObj3.userId = "1205"
         userJourneyObj3.timeStamp = 30
            store = userJourneyObj3 +: store
            
           
           var userJourneyObj4= new UserJourney()
         userJourneyObj4.channelName="conversion"
         userJourneyObj4.sales = 120.0
         userJourneyObj4.userId = "1205"
         userJourneyObj4.timeStamp = 40
            store = userJourneyObj4 +: store
            
            
           
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            var listObj =  (heuristicObj.callLastClick("1205", store.toIterable, salesObj, weightAssignmentObj))
            var sum : Double = 0
            for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }
            assert(sum == 1.0) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
      //*******************************************
         
         
         
         "TestCase" should "pass if findSinglePath as it gives the userJourney objects in sorted order" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="facebook"
         userJourneyObj.timeStamp = 10
         userJourneyObj.sales = 160
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="vtDisplay"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "1205"
         userJourneyObj2.timeStamp = 5
            store = userJourneyObj2 +: store
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
           
             var userJourneyObj3= new UserJourney()
         userJourneyObj3.channelName="GoogleBrand"
         userJourneyObj3.sales = 120.0
         userJourneyObj3.userId = "1205"
         userJourneyObj3.timeStamp = 6
            store = userJourneyObj3 +: store
            
           
           var userJourneyObj4= new UserJourney()
         userJourneyObj4.channelName="conversion"
         userJourneyObj4.sales = 120.0
         userJourneyObj4.userId = "1205"
         userJourneyObj4.timeStamp = 40
            store = userJourneyObj4 +: store
            
          var userJourneyObj5= new UserJourney()
         userJourneyObj5.channelName="ct_dispaly"
         userJourneyObj5.sales = 120.0
         userJourneyObj5.userId = "1205"
         userJourneyObj5.timeStamp = 50
            store = userJourneyObj5 +: store
            
            
            var userJourneyObj6= new UserJourney()
         userJourneyObj6.channelName="conversion"
         userJourneyObj6.sales = 120.0
         userJourneyObj6.userId = "1205"
         userJourneyObj6.timeStamp = 60
            store = userJourneyObj6 +: store
           
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            /*var listObj =  (heuristicObj.delegateHeuristicsType("1205", store.toIterable, salesObj, weightAssignmentObj) )
            var sum : Double = 0
            for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }
            assert(sum == 1.0) */
           
           
           
           var outputcheck :List[UserJourney] = null
           outputcheck = heuristicObj.findSinglePath("1205", store.toIterable)
           var countConversion : Int = 0
           for(ctr<- 0 to outputcheck.length -1){
             
             if(outputcheck(ctr).channelName.equalsIgnoreCase("conversion")  ){
             
               countConversion = countConversion+1
              
               }
             
             }
           var checkisLarger:Boolean = true
           if(outputcheck.length > 1){
           for(ctr<- 0 to outputcheck.length -2){
             
             if(outputcheck(ctr).timeStamp >=  outputcheck(ctr+1).timeStamp ){
             
               checkisLarger = true
              
               }
             else
               checkisLarger = false
             
             }
           
           }
           assert(checkisLarger == true) 
           
           assert(outputcheck.length== 4) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
           
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         //**********************************
       /*
        * The test case checks if the findSinglePath method considers only the first path in the List of user
        * journey where more than one conversion is there
        * */
         "TestCase" should "pass in findSinglePath" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="facebook"
         userJourneyObj.timeStamp = 10
         userJourneyObj.sales = 160
         userJourneyObj.userId = "123"
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="vtDisplay"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "1205"
         userJourneyObj2.timeStamp = 20
            store = userJourneyObj2 +: store
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
           
             var userJourneyObj3= new UserJourney()
         userJourneyObj3.channelName="GoogleBrand"
         userJourneyObj3.sales = 120.0
         userJourneyObj3.userId = "1205"
         userJourneyObj3.timeStamp = 30
            store = userJourneyObj3 +: store
            
           
           var userJourneyObj4= new UserJourney()
         userJourneyObj4.channelName="conversion"
         userJourneyObj4.sales = 120.0
         userJourneyObj4.userId = "1205"
         userJourneyObj4.timeStamp = 40
            store = userJourneyObj4 +: store
            
          var userJourneyObj5= new UserJourney()
         userJourneyObj5.channelName="ct_dispaly"
         userJourneyObj5.sales = 120.0
         userJourneyObj5.userId = "1205"
         userJourneyObj5.timeStamp = 50
            store = userJourneyObj5 +: store
            
            
            var userJourneyObj6= new UserJourney()
         userJourneyObj6.channelName="conversion"
         userJourneyObj6.sales = 120.0
         userJourneyObj6.userId = "1205"
         userJourneyObj6.timeStamp = 60
            store = userJourneyObj6 +: store
           
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            /*var listObj =  (heuristicObj.delegateHeuristicsType("1205", store.toIterable, salesObj, weightAssignmentObj) )
            var sum : Double = 0
            for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }
            assert(sum == 1.0) */
           
           
           
           var outputcheck :List[UserJourney] = null
           outputcheck = heuristicObj.findSinglePath("1205", store.toIterable)
           var countConversion : Int = 0
           for(ctr<- 0 to outputcheck.length -1){
             
             if(outputcheck(ctr).channelName.equalsIgnoreCase("conversion")  ){
             
               countConversion = countConversion+1
              
               }
             
             }
           
           
           assert(countConversion == 1) 
           
           assert(outputcheck.length== 4) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
           
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
         
         
         /*The method findSinglePath checks if in the occurance of conversion at the first instance is taken care 
          * of , it should return only one user journey object in the List
          * 
          * */
         
         
         "TestCase" should "pass in findSinglePath as extra rows are not taken into consideration" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
         
            
           
           var userJourneyObj4= new UserJourney()
         userJourneyObj4.channelName="conversion"
         userJourneyObj4.sales = 120.0
         userJourneyObj4.userId = "1205"
         userJourneyObj4.timeStamp = 40
            store = userJourneyObj4 +: store
            
          var userJourneyObj5= new UserJourney()
         userJourneyObj5.channelName="ct_dispaly"
         userJourneyObj5.sales = 120.0
         userJourneyObj5.userId = "1205"
         userJourneyObj5.timeStamp = 50
            store = userJourneyObj5 +: store
            
            
            var userJourneyObj6= new UserJourney()
         userJourneyObj6.channelName="conversion"
         userJourneyObj6.sales = 120.0
         userJourneyObj6.userId = "1205"
         userJourneyObj6.timeStamp = 60
            store = userJourneyObj6 +: store
           
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            /*var listObj =  (heuristicObj.delegateHeuristicsType("1205", store.toIterable, salesObj, weightAssignmentObj) )
            var sum : Double = 0
            for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }
            assert(sum == 1.0) */
           
           
           
           var outputcheck :List[UserJourney] = null
           outputcheck = heuristicObj.findSinglePath("1205", store.toIterable)
           var countConversion : Int = 0
           for(ctr<- 0 to outputcheck.length -1){
             
             if(outputcheck(ctr).channelName.equalsIgnoreCase("conversion")  ){
             
               countConversion = countConversion+1
              
             }
             
             }
           
           
           assert(countConversion == 1) 
           
           assert(outputcheck.length== 1) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
           
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
         
         
         
         
         
         
         
         
         
         
         "TestCase" should "pass in findSinglePath for direct case" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
          
           var userJourneyObj4= new UserJourney()
         userJourneyObj4.channelName="conversion"
         userJourneyObj4.sales = 120.0
         userJourneyObj4.userId = "1205"
         userJourneyObj4.timeStamp = 40
            store = userJourneyObj4 +: store
            
                 
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            /*var listObj =  (heuristicObj.delegateHeuristicsType("1205", store.toIterable, salesObj, weightAssignmentObj) )
            var sum : Double = 0
            for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }
            assert(sum == 1.0) */
           
           
           
           var outputcheck :List[UserJourney] = null
           outputcheck = heuristicObj.findSinglePath("1205", store.toIterable)
              
                   
           assert(outputcheck.length== 1) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
           
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
         /*
          * The Test case checks if the direct case of user conversion is handeled by returning a null value
          * */
         
         "TestCase" should "pass for direct case in PB" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
          
           
           var userJourneyObj= new UserJourney()
         userJourneyObj.channelName="conversion"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "1205"
         userJourneyObj.timeStamp = 40
         store = userJourneyObj +: store
            
          
           
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
           
           
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
            var listObj =  (heuristicObj.callLastClick("1205", store.toIterable, salesObj, weightAssignmentObj))
             var sum : Double = 0
            /*for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }*/
            assert(listObj  == null) 
           
             
             
           
           
          
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
           
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
         
         
       "TestCase" should "pass for direct case in LC" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionLastClick()
          
           
           var userJourneyObj= new UserJourney()
         userJourneyObj.channelName="conversion"
         userJourneyObj.sales = 120.0
         userJourneyObj.userId = "1205"
         userJourneyObj.timeStamp = 40
         store = userJourneyObj +: store
            
          
           
           
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 0
           
           
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
            var listObj =  (heuristicObj.callLastClick("1205", store.toIterable, salesObj , weightAssignmentObj) )
            var sum : Double = 0
            /*for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }*/
            assert(listObj  == null) 
           
             
             
           
           
          
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
           
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }  
         
         
         
         
         
        /* "TestCase" should "pass as we have tested three channles case" in {
        
        
           
           
           var store = new ListBuffer[UserJourney]()

           
           var heuristicObj = new HeuristicChannelAttributionPositionBased()
           var userJourneyObj = new UserJourney()
         userJourneyObj.channelName="facebook"
         userJourneyObj.sales = 160
         userJourneyObj.userId = "123"
         userJourneyObj.timeStamp= 10
         store = userJourneyObj +: store
           var userJourneyObj2= new UserJourney()
         userJourneyObj2.channelName="vtDisplay"
         userJourneyObj2.sales = 120.0
         userJourneyObj2.userId = "1205"
         userJourneyObj2.timeStamp= 20
            store = userJourneyObj2 +: store
            var salesObj = new SalesData()
           salesObj.userId = "1205"
           salesObj.timeStamp = 0
           salesObj.salesValue = 1000
           
            var userJourneyObj3= new UserJourney()
         userJourneyObj3.channelName="Googlesearch"
         userJourneyObj3.sales = 130.0
         userJourneyObj3.userId = "1205"
         userJourneyObj3.timeStamp = 40
            store = userJourneyObj3 +: store
            
             var userJourneyObj4= new UserJourney()
         userJourneyObj4.channelName="conversion"
         userJourneyObj4.sales = 130.0
         userJourneyObj4.userId = "1205"
         userJourneyObj4.timeStamp=50
            store = userJourneyObj4 +: store
           var weightAssignmentObj =  new HeuristicsWeightsAssignment()
           weightAssignmentObj.originatorWeight = 0.3
           weightAssignmentObj.convertorWeight = 0.5
           weightAssignmentObj.intermediateWeight = 0.2
           heuristicObj.whichMethod = 1
            var listObj =  (heuristicObj.callPositionBased("1205", store.toIterable, salesObj, weightAssignmentObj))
            var sum : Double = 0
            for(i <- 0 to listObj.length -1){
              
              sum = sum+listObj(i).channelWeight
              
            }
            assert(sum == 1.0) 
            //assert(listObj.isInstanceOf[List[ChannelWeightsAtGranularlevel]])      
        
      }
      */
      
   /*  "BootstrapTest" should "pass this test, because it valildates  invalid jobType as null" in {
           val userInput = null
           val testbootstrap = new Bootstrap(userInput)
           // testbootstrap.isValidInput()
           assert(testbootstrap.isValidInput()=== false)
     }
      
       
     "BootstrapTest" should "pass this test, because it valildates context loaded" in {
           val testbootstrap = new Bootstrap("1")
           val mocked = Mockito.mock(classOf[ApplicationInstance])
           
           testbootstrap.applicationInstance = mocked
           testbootstrap.loadContext()
            
           val bean = ApplicationUtil.retrieveBean("dataIngestionJobFactory") 
          
*/  
}


