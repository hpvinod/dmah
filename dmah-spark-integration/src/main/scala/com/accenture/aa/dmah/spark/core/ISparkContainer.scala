package com.accenture.aa.dmah.spark.core

import com.accenture.aa.dmah.nosql.writer.IBatchContainer
import com.accenture.aa.dmah.nosql.service.INoSQLService

/**This interface holds the reference to spark drive and the spark context 
 * 
 * Used to broadcast the object that need to run inside spark job
 * 
 * @author kanwar.singh
 * */

trait ISparkContainer  extends Serializable{
	
	def broadcastNoSqlService (noSqlService: INoSQLService)
	
	def broadcastBatchWriter(batchContainerObj: IBatchContainer)
}