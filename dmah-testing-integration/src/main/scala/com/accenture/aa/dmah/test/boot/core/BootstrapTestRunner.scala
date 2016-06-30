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
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BootstrapTestRunner extends FlatSpec with Matchers with MockitoSugar {

      "BootstrapTest" should "pass this test, because it valildates invalid jobtype as Int" in {
         val userInput = "dataJobRunner"
         val testbootstrap = new Bootstrap(userInput)
         testbootstrap.initContext()
         testbootstrap.isValidInput()
         assert(testbootstrap.isValidInput()=== false)
       }
      "BootstrapTest" should "pass this test, because it valildates  invalid jobType as String" in {
           val userInput = "jobtype"
           val testbootstrap = new Bootstrap(userInput)
            testbootstrap.isValidInput()
            testbootstrap.initContext()
           assert(testbootstrap.isValidInput()=== false)
     }
    /* "BootstrapTest" should "pass this test, because it valildates  invalid jobType as null" in {
           val userInput = null
           val testbootstrap = new Bootstrap(userInput)
            testbootstrap.isValidInput()
           assert(testbootstrap.isValidInput()=== false)
     }*/
      
       
     "BootstrapTest" should "pass this test, because it valildates context loaded" in {
           val testbootstrap = new Bootstrap("attributionJobRunner")
           val mocked = Mockito.mock(classOf[ApplicationInstance])
           
           testbootstrap.applicationInstance = mocked
           testbootstrap.initContext()
           testbootstrap.boot()
            
           val bean = ApplicationUtil.retrieveBean("dataIngestionJobRunner") 
          
           assert(bean.getClass().getName==="com.accenture.aa.dmah.dataingestion.jobs.DataIngestionJobFactory")
     }
         
 }

 
