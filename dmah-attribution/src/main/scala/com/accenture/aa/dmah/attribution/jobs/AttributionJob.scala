package com.accenture.aa.dmah.attribution.jobs

import org.slf4j.LoggerFactory
import com.accenture.aa.dmah.attribution.core.bo.AttributionCommand
import com.accenture.aa.dmah.attribution.core.IAttributionHub
import com.accenture.aa.dmah.core.DMAHJob
import scala.beans.BeanProperty
import com.accenture.aa.dmah.attribution.util.AttributionTechnique
import com.accenture.aa.dmah.core.GlobalContext

/**
 * This class responsible to perform business logic for Attribution job
 *
 */

class AttributionJob extends DMAHJob {
  val attributionLogger = LoggerFactory.getLogger(classOf[AttributionJob])

  @BeanProperty
  var attributionHub: IAttributionHub = _

  /**
   * Method to implement business logic
   */
  override def process(jobId:String) = {
    attributionLogger.info("in process of AttributionJob")

    val attributionCommand = new AttributionCommand
    attributionCommand.modellingTechnique = GlobalContext.mappingProperties.get("attributionJobRunner").get.getProperty("modellingTechnique")

    attributionHub.executeAttribution(attributionCommand)
  }

}