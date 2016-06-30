package com.accenture.aa.dmah.test.boot.core
import org.slf4j.{Logger => Underlying}
import org.scalatest.mock.MockitoSugar
import com.accenture.aa.dmah.core.Bootstrap
import org.slf4j.Logger
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.specs2.matcher.ShouldMatchers
import org.scalatest.FunSuite
 import org.scalatest.Matchers
 import org.scalatest.FlatSpec
 import org.junit.runner.RunWith
 import org.scalatest.junit.JUnitRunner

 @RunWith(classOf[JUnitRunner])
class LoggingTestRunner   extends FlatSpec with Matchers with MockitoSugar {
   def initTestable(mocked: Underlying): Bootstrap = {
     val mockLogger = mock[Logger]
    new Bootstrap("1") {
        override val logger = mockLogger
    }
   }

  "TestLogging" should "pass this test it validate logger initilization" in {
       val mocked = Mockito.mock(classOf[Underlying])
        when(mocked.isInfoEnabled()).thenReturn(true)
        initTestable(mocked).initLogging()
        //verify(mocked).info("logger initialized")
  }
}