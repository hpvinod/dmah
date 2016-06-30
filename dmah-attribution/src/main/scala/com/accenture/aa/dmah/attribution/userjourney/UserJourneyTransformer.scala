package com.accenture.aa.dmah.attribution.userjourney

import scala.beans.BeanProperty
import org.apache.spark.sql.DataFrame

class UserJourneyTransformer extends IUserJourneyTransformer {
 
  @BeanProperty
  var mulltipleUserJourneyTransformer: IUserJourneyTransformer = _
  
  def transformUserJourney(userJourney: DataFrame):DataFrame = {
    
    null
  }
  
}