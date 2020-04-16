package utils

import java.io.File

import org.apache.commons.io.FileUtils
import implicits.Implicits._

object Utils {

  val windowsPath="F:/Eggplant/files"
  val linuxPath="/root/eggplant/files"

//  def orderCallLinuxScript(tmpDir: File, shBuffer: List[String]): ExecCommand = {
//    val runFile = new File(tmpDir, s"run.sh")
//    val pidFile = new File(tmpDir, "pid.txt")
//    val pidCommand =
//      s"""
//         |echo $$$$ > ${pidFile.unixPath}
//         |""".stripMargin
//    val deletePidCommand =
//      s"""
//         |rm ${pidFile.unixPath}
//         |""".stripMargin
//    val newBuffer = List(pidCommand) ::: shBuffer ::: List(deletePidCommand)
//    val shStr = newBuffer.flatMap { line =>
//      line.split("\r\n")
//    }.notEmptyLines.notAnnoLines.mkString(" &&\\\n")
//    (shStr + "\n").toFile(runFile)
//    val dos2Unix = s"${("dos2unix").wsl} ${runFile.unixPath} "
//    val shCommand = s"${("sh").wsl} ${runFile.unixPath}"
//    (ExecCommand()).exec(dos2Unix, shCommand, tmpDir)
//  }

  val path:String={
    if(new File(windowsPath).exists()) windowsPath else linuxPath
  }

  val suffix: String = {
    if (new File(windowsPath).exists()) ".exe" else " "

  }

  def deleteDirectory(direcotry: String) = {
    try {
      FileUtils.deleteDirectory(new File(direcotry))
    } catch {
      case _: Throwable =>
    }
  }

  def deleteDirectory(direcotry: File) = {
    try {
      FileUtils.deleteDirectory(direcotry)
    } catch {
      case _: Throwable =>
    }
  }

  val scriptHtml =
    """
      |<script>
      |  $(function () {
      |        $("footer:first").remove()
      |        $("#content").css("margin","0")
      |       $(".linkheader>a").each(function () {
      |        var text=$(this).text()
      |        $(this).replaceWith("<span style='color: #222222;'>"+text+"</span>")
      |       })
      |
      |      $("tr").each(function () {
      |         var a=$(this).find("td>a:last")
      |      var text=a.text()
      |      a.replaceWith("<span style='color: #222222;'>"+text+"</span>")
      |     })
      |
      |       $("p.titleinfo>a").each(function () {
      |        var text=$(this).text()
      |        $(this).replaceWith("<span style='color: #606060;'>"+text+"</span>")
      |       })
      |
      |       $(".param:eq(1)").parent().hide()
      |       $(".linkheader").hide()
      |
      |    })
      |</script>
  """.stripMargin



}
