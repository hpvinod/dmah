package com.accenture.aa.dmah.core.loaders
import scala.collection.JavaConversions._
import java.util.List

/**
 * 
 */
class LoaderList {
  
  var list: List[ILoader] = _ ;
 
  def setList(list: List[ILoader]) {
    this.list = list
  }
}