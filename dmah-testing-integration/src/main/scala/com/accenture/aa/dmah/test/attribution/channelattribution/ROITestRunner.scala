package com.accenture.aa.dmah.test.attribution.channelattribution

import org.scalatest.mock.MockitoSugar
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import com.accenture.aa.dmah.core.ApplicationInstance
import com.accenture.aa.dmah.core.GlobalContext
import com.accenture.aa.dmah.spark.core.SparkDriver
import com.accenture.aa.dmah.core.Bootstrap
import org.mockito.Mockito
import com.accenture.aa.dmah.spark.controller.DataFrameController
import com.accenture.aa.dmah.spark.core.CacheType
import org.apache.spark.sql.DataFrame
import scala.collection.mutable.ListBuffer
import com.accenture.aa.dmah.attribution.channelattribution.bo.ChannelWeightsAtGranularLevel
import com.accenture.aa.dmah.attribution.channelattribution.HeuristicsChannelAttribution
import com.accenture.aa.dmah.attribution.channelattribution.bo.ROI
import com.accenture.aa.dmah.attribution.channelattribution.bo.Investment
import com.accenture.aa.dmah.core.ApplicationInstance
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.core.ApplicationInstance
import org.apache.spark.sql.DataFrame



class ROITestRunner extends FlatSpec with MockitoSugar with Matchers {
  
  "ROITester" should "pass the test" in {
    
    /*
		val testbootstrap = new Bootstrap("1");
		*//**
		 * Creating mock object of application instance 
		 * *//*
		
		val mockedAppInstance = Mockito.mock(classOf[ApplicationInstance])
    testbootstrap.applicationInstance = mockedAppInstance
		
    *//**
		 * This method will load the applicationContext in to context
		 * *//*
    
    testbootstrap.loadContext()
    
    *//**
     * Getting spark driver bean from applicationContext
     * *//*
    
    val sparkDriver = GlobalContext.applicationContext.getBean("sparkDriver").asInstanceOf[SparkDriver];
		
		*//**
		 * Initializing spark context and sql context
		 * *//*
		
		sparkDriver.sc = Mockito.mock(classOf[SparkContext])
		sparkDriver.sqlContext = Mockito.mock(classOf[HiveContext])
		sparkDriver.nosqlInitialization = Mockito.mock(classOf[NoSqlInitialization])
		sparkDriver.initializeSparkContext()
		
		println("Spark Context : "+sparkDriver.sc)*/
		
		
		 /*var store = new ListBuffer[(String, ChannelWeightsAtGranularLevel) ]()
		 
		 var channelWeightsAtGranularLevelObjA = new ChannelWeightsAtGranularLevel()
		channelWeightsAtGranularLevelObjA.attributedSales = 100
		channelWeightsAtGranularLevelObjA.channelName = "VtDisplay"
		store = ("VtDispaly ",channelWeightsAtGranularLevelObjA) +: store
		
		
		 var channelWeightsAtGranularLevelObjB= new ChannelWeightsAtGranularLevel()
		channelWeightsAtGranularLevelObjB.attributedSales = 100
		channelWeightsAtGranularLevelObjB.channelName = "CtDisplay"
    store = ("CtDispaly ",channelWeightsAtGranularLevelObjB) +: store       
           */
		
		val testbootstrap = new Bootstrap("1");
		/**
		 * Creating mock object of application instance 
		 * */
		
		val mockedAppInstance = Mockito.mock(classOf[ApplicationInstance])
    testbootstrap.applicationInstance = mockedAppInstance
		
    /**
		 * This method will load the applicationContext in to context
		 * */
    
    testbootstrap.initContext()
    testbootstrap.boot()
    
    /**
     * Getting spark driver bean from applicationContext
     * */
    
    val sparkDriver = GlobalContext.applicationContext.getBean("sparkDriver").asInstanceOf[SparkDriver];
		
		/**
		 * Initializing spark context and sql context
		 * */
		
		sparkDriver.initializeSparkContext();
		
		var store = new ListBuffer[(String, ChannelWeightsAtGranularLevel) ]()
		 
		 var channelWeightsAtGranularLevelObjA = new ChannelWeightsAtGranularLevel()
		channelWeightsAtGranularLevelObjA.attributedSales = 100
		channelWeightsAtGranularLevelObjA.channelName = "VtDisplay"
		store = ("VtDisplay",channelWeightsAtGranularLevelObjA) +: store
		
		
		 var channelWeightsAtGranularLevelObjB= new ChannelWeightsAtGranularLevel()
		channelWeightsAtGranularLevelObjB.attributedSales = 100
		channelWeightsAtGranularLevelObjB.channelName = "CtDisplay"
    store = ("CtDisplay",channelWeightsAtGranularLevelObjB) +: store       
     
    
    var storeInvestment = new ListBuffer[Investment ]()
      var investmentObjA = new Investment(); 
		investmentObjA.channelName = "VtDisplay"
		investmentObjA.investment = 100
		storeInvestment = investmentObjA +: storeInvestment
		var investmentObjB = new Investment(); 
		investmentObjB.channelName = "CtDisplay"
		investmentObjB.investment = 100
	  storeInvestment = investmentObjB +: storeInvestment

		
		
		var  granularRDD = sparkDriver.sc.parallelize(store)
		println("granularRDD"+granularRDD.count())
		
		
		var  investmentRDD = sparkDriver.sc.parallelize(storeInvestment)
		println("granularRDD"+investmentRDD.count())
		
		var HeuChanAttrObj = new HeuristicsChannelAttribution()
		
		HeuChanAttrObj.reduceByChannel = granularRDD
		HeuChanAttrObj.investmentRddProcesed = investmentRDD
		HeuChanAttrObj.getROI(null)
		var roiListObj: ListBuffer[ROI] = null
		roiListObj = HeuChanAttrObj.roiList
	  var lengthRoi = roiListObj.length 
	  for(i <- 0 to lengthRoi -1 ){
	    
	    println(" roi : "+roiListObj(i))
	  }
	  
	  assert(lengthRoi == 2)
	  assert(roiListObj(0).ROI == 1)
	  assert(roiListObj(1).ROI == 1)
		
  }
}