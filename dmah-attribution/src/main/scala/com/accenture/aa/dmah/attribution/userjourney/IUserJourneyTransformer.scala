package com.accenture.aa.dmah.attribution.userjourney

import org.apache.spark.sql.DataFrame

trait IUserJourneyTransformer {
  def transformUserJourney(userJourney: DataFrame):DataFrame
}