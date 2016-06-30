package com.accenture.aa.dmah.test.sparkintegration.core

import org.scalatest.mock.MockitoSugar
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import com.accenture.aa.dmah.core.ApplicationInstance
import com.accenture.aa.dmah.core.GlobalContext
import com.accenture.aa.dmah.spark.core.SparkDriver
import com.accenture.aa.dmah.core.Bootstrap
import org.mockito.Mockito
import java.util.Arrays.ArrayList
import java.util.ArrayList
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.Row
import com.accenture.aa.dmah.spark.controller.DataFrameController
import com.accenture.aa.dmah.spark.core.CacheType
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.DataFrame
import com.accenture.aa.dmah.nosql.core.NoSqlInitialization
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class DataFrameControllerTestRunner extends FlatSpec with MockitoSugar with Matchers {
  
  "Data frame controller" should "cache the given dataset as per the cache type" in {
    
    
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
		sparkDriver.initializeSparkContext()
		
		val dfController = new DataFrameController()
		
		/**
		 * creating a new data frame with one column employee id
		 * */

		val df = Mockito.mock(classOf[DataFrame])
		dfController.persistDataSet(df,CacheType.MEMORY )
		
		//assert(df.getStorageLevel.useMemory == true)
    
  }
}