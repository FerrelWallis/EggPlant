package tool

import java.io.File
import java.nio.file.Files

import dao.ModeDao
import utils.Utils

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object Tool {

  val testDir=new File("/root/eggplant/files/temp")

  def execFuture[T](f: Future[T]): T = {
    Await.result(f, Duration.Inf)
  }

  def createTempDirectory(prefix: String)(implicit modeDao: ModeDao) = {
    if (isTestMode) Tool.testDir else Files.createTempDirectory(prefix).toFile
  }

  def isTestMode(implicit modeDao: ModeDao) = {
    val mode = execFuture(modeDao.select)
    println(mode)
    if (mode.test=="t") true else false
  }

}
