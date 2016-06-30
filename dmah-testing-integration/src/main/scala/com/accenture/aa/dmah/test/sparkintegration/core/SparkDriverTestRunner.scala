package com.accenture.aa.dmah.test.sparkintegration.core

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import com.accenture.aa.dmah.spark.core.SparkDriver
import org.apache.spark.SparkContext
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.when
import java.util.HashMap
import com.accenture.aa.dmah.core.GlobalContext
import com.accenture.aa.dmah.core.ApplicationInstance
import com.accenture.aa.dmah.core.Bootstrap
import org.apache.spark.sql.hive.HiveContext
import com.accenture.aa.dmah.nosql.core.NoSQLInitializationInSpark
import org.mockito.internal.util.reflection.Whitebox
import com.accenture.aa.dmah.nosql.core.NoSqlInitialization
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SparkDriverTestRunner extends FlatSpec with Matchers with MockitoSugar{

	"Spark Driver" should "create spark context and sql context object successfully" in {

		val testbootstrap = new Bootstrap("attributionJobRunner");
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
		sparkDriver.sc = Mockito.mock(classOf[SparkContext])
		sparkDriver.sqlContext = Mockito.mock(classOf[HiveContext])
		sparkDriver.nosqlInitialization = Mockito.mock(classOf[NoSqlInitialization])
		sparkDriver.initializeSparkContext();
		
		when(sparkDriver.sc.appName).thenReturn("DMAH-application")
		assert(sparkDriver.sc.appName == "DMAH-application");
   }
}