package com.accenture.aa.dmah.test.boot.core

import org.mockito.Matchers.{ eq => mockEq }
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.prop.Whenever

import com.accenture.aa.dmah.core.Bootstrap
import org.slf4j.Logger
import org.mockito.Mockito
import org.mockito.Mockito.verify

import org.specs2.Specification
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfterEach
import com.accenture.aa.dmah.core.ApplicationInstance
import com.accenture.aa.dmah.dataingestion.jobs.DataIngestionJobFactory
import com.accenture.aa.dmah.core.util.ApplicationUtil
import org.springframework.context.ApplicationContext
import com.accenture.aa.dmah.core.GlobalContext
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.when
import com.accenture.aa.dmah.core.JobRunner
import org.apache.hadoop.mapreduce.v2.api.records.JobReport
import com.accenture.aa.dmah.core.ApplicationInstance
import com.accenture.aa.dmah.core.ConfigRunner
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ApplicationInstanceTestRunner extends FlatSpec with Matchers with MockitoSugar {

      "ApplicationTestRunner" should "pass this test, because it validate return type as DataIngestion job runner" in {
 
         /**
          * creating bootstrap object to load context object
          */
           val testbootstrap = new Bootstrap("attributionJobRunner")
           val mockedAppInstance = Mockito.mock(classOf[ApplicationInstance])
         
           var app = new ApplicationInstance()
                  
           testbootstrap.applicationInstance = mockedAppInstance
           testbootstrap.initContext()
           testbootstrap.boot()
         
           
           val mockedFactory = Mockito.mock(classOf[JobRunner])
         
           doReturn(mockedFactory).when(mockedAppInstance).initilize(GlobalContext.applicationContext, "dataIngestionJobRunner")
      }
      
      
      /*"ApplicationTestRunner" should "pass this test,it initilize sparkcontext" in {
         
         *//**
          * creating bootstrap object to load context object
          *//*
           val testbootstrap = new Bootstrap("1")
           val mockedAppInstance = Mockito.mock(classOf[ApplicationInstance])
         
           var configRunner = new ConfigRunner()
                  
           testbootstrap.applicationInstance = mockedAppInstance
           testbootstrap.loadContext()
           
           configRunner.configure(1)
           assert(GlobalContext.sparkContext.appName==="DMAH-application")
      }
         */
 }

 