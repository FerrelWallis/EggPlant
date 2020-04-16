package test

import java.io.File
import java.nio.file.Files

import implicits.Implicits._
import utils.Utils
import utils.ExecCommand

object Test {

  def main(args: Array[String]): Unit = {

    val tmpDir = new File("E:\\temp")
    val seqFile = new File(tmpDir, "seq.fa")
    val blastType="blastn"
    val db="cds/egg"
    val seqlist=tmpDir+"/seqlist.txt"
    val evalue="1e-5"
    val maxTargetSeqs="2"
    val wordSize="28"
    val outXml = new File(tmpDir, "out.xml")

    println(tmpDir)
    println(seqFile.unixPath)
    println(db)
    println(seqlist.unixPath)
    println(outXml.unixPath)

    ///

    val command=
      s"""|${Utils.path.unixPath}/tools/blastTools/ncbi-blast-2.6.0+/bin/${blastType} -query ${seqFile.unixPath} -db ${db} -seqidlist ${seqlist.unixPath} -outfmt 5 -evalue ${evalue} -max_target_seqs ${maxTargetSeqs} -word_size ${wordSize} -out ${outXml.unixPath}
          |""".stripMargin

    //execCommand.exec(command,new File(tmpDir))
//
//    val execCommand=orderCallLinuxScript(tmpDir,List(command))
//    println(execCommand.getLogStr)

  }


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







}
