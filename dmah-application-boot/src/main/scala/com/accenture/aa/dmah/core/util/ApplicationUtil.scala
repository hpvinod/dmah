package com.accenture.aa.dmah.core.util

import org.springframework.context.ApplicationContext
import com.accenture.aa.dmah.core.GlobalContext
 
/**
 * This singleton class is used to define utility methods 
 * 
 * @author payal.patel
 */
object ApplicationUtil {
  
  //  @throws(classOf[NoBeanDefinedException])  
    def retrieveBean(beanName:String): Any = {
      GlobalContext.applicationContext.getBean(beanName)
	}
}