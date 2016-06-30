package com.accenture.aa.dmah.core

import org.apache.hadoop.fs.{ FileSystem, Path }
import org.apache.spark.sql.DataFrame
import org.apache.spark.rdd.RDD
import java.io.File
import org.apache.spark.sql.Row
import scala.collection.mutable.ListBuffer
import java.io.{ BufferedInputStream, BufferedOutputStream, BufferedWriter, OutputStreamWriter }
import java.text.SimpleDateFormat
import java.util.Date

object HdfsWriter extends Serializable {

  val hdfs: FileSystem = GlobalContext.hdfs
  val hdfsUri = GlobalContext.hdfsURI
  val outputFormat: String = "com.databricks.spark.csv"
  var dateFormat = new SimpleDateFormat("DDMMyyyyHHmmssSSS");

  /**
   * @author arpit.j.jain
   * @param dataToWrite:Object
   * @param fileName:String
   * @param folder:String
   * @param partitions:Integer
   *
   * This method will write data as a CSV into the HDFS. folder needs to be of format "/tmp/". partitions should be 1 for small dataset
   *
   */
  def writeDFToHdfs(dataToWrite: DataFrame, hdfsfileName: String, hdfsfolder: String, partitions: Integer) = {

    var timestamp = dateFormat.format(new Date())

    println("Starting to write data into HDFS at location " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

    val hdfsTempFolder = hdfsfolder + "tempFolder"
    hdfs.delete(new Path(hdfsfolder + hdfsfileName), true)
    dataToWrite.repartition(partitions)
      .write.format(outputFormat)
      .option("header", "true")
      .save(hdfsUri + hdfsTempFolder)
    val pathOld = new Path(hdfsTempFolder)
    val tmpPath = new Path(hdfsTempFolder + File.separatorChar + "part-00000")
    val path = new Path(hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

    hdfs.rename(tmpPath, path)
    hdfs.delete(pathOld, true)

    println("Succefully writen data into " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

  }

  def writeRDDToHdfs[T <: Object](dataToWrite: RDD[T], hdfsfileName: String, hdfsfolder: String, partitions: Integer) = {

    var timestamp = dateFormat.format(new Date())
    println("Starting to write data into HDFS at location " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

    val hdfsTempFolder = hdfsfolder + "tempFolder"
    hdfs.delete(new Path(hdfsfolder + hdfsfileName), true)
    dataToWrite.repartition(partitions).saveAsTextFile(hdfsUri + hdfsTempFolder)

    val pathOld = new Path(hdfsTempFolder)
    val path = new Path(hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")
    val tmpPath = new Path(hdfsTempFolder + File.separatorChar + "part-00000")

    hdfs.rename(tmpPath, path)
    hdfs.delete(pathOld, true)

    println("Succefully writen data into " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

  }

  def writeListToHdfs[T <: Object](dataToWrite: ListBuffer[T], hdfsfileName: String, hdfsfolder: String) = {

    var timestamp = dateFormat.format(new Date())
    println("Starting to write data into HDFS at location " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

    val path = new Path(hdfsfolder + hdfsfileName)
    hdfs.delete(path, true)
    val output = hdfs.create(new Path(hdfsfolder + hdfsfileName + "_" + timestamp + ".csv"))
    val br: BufferedWriter = new BufferedWriter(new OutputStreamWriter(output));
    for (ctr <- 0 to dataToWrite.length - 1) {
      var roiObj = dataToWrite(ctr)
      br.write(roiObj.toString())
      br.write("\n")
    }

    br.close()

    println("Succefully writen data into " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

  }

  def writeMapToHdfs(dataToWrite: Map[String, Double], hdfsfileName: String, hdfsfolder: String) = {

    var timestamp = dateFormat.format(new Date())
    println("Starting to write data into HDFS at location " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

    val path = new Path(hdfsfolder + hdfsfileName)
    hdfs.delete(path, true)
    val output = hdfs.create(new Path(hdfsfolder + hdfsfileName + "_" + timestamp + ".csv"))
    val br: BufferedWriter = new BufferedWriter(new OutputStreamWriter(output));
    for ((mapKey, mapValue) <- dataToWrite) {
      br.write(mapKey + "," + mapValue)
      br.write("\n")
    }

    br.close()

    println("Succefully writen data into " + hdfsfolder + hdfsfileName + "_" + timestamp + ".csv")

  }

}