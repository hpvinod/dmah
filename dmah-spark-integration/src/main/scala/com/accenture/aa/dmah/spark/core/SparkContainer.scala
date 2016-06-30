package com.accenture.aa.dmah.spark.core

import java.io.Serializable

import scala.beans.BeanProperty

import org.apache.spark.broadcast.Broadcast

import com.accenture.aa.dmah.nosql.service.INoSQLService
import com.accenture.aa.dmah.nosql.writer.IBatchWriter
import com.accenture.aa.dmah.nosql.core.NoSQLInitializationInSpark
import com.accenture.aa.dmah.nosql.writer.IBatchContainer

/**This class holds the reference to spark drive and the spark context 
 * 
 * Used to broadcast the object that need to run inside spark job
 * 
 * @author kanwar.singh
 * */

class SparkContainer extends ISparkContainer{
  
  var noSqlServiceImpl : Broadcast[INoSQLService] = _
  
  var batchContainerBroadcast : Broadcast[IBatchContainer]= _
  
  @BeanProperty
  var batchContainer : IBatchContainer= _
 
  @BeanProperty
  var sparkDriver : SparkDriver = _
  
  def broadcastNoSqlService (noSqlService: INoSQLService){
    val sc = sparkDriver.sc
		noSqlServiceImpl = sc.broadcast(noSqlService);
  }
  
  def broadcastBatchWriter(batchContainerObj: IBatchContainer){
    val sc = sparkDriver.sc
	batchContainerBroadcast = sc.broadcast(batchContainerObj)
  }
   
  
}