package controllers

import java.io.File
import java.nio.file.Files

import dao.ModeDao
import javax.inject.Inject
import org.apache.commons.io.FileUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import utils.{ExecCommand, TableUtils, Utils}

import scala.collection.JavaConverters._
import utils.Blast._

import scala.concurrent.ExecutionContext
import implicits.Implicits._
import tool.Tool

class BlastController @Inject()(cc: ControllerComponents)(implicit exec: ExecutionContext,
                                                          implicit val modeDao: ModeDao
) extends AbstractController(cc){
  def blastBefore() = Action { implicit request =>
    Ok(views.html.blast.blast())
  }

  case class blastData(blastType: String, method: String, queryText: String,evalue: String,
                       wordSize: String, maxTargetSeqs: String)

  val blastForm = Form(
    mapping(
      "blastType" -> text,
      "method" -> text,
      "queryText" -> text,
      "evalue" -> text,
      "wordSize" -> text,
      "maxTargetSeqs" -> text
    )(blastData.apply)(blastData.unapply)
  )

  def blastRun = Action(parse.multipartFormData) { implicit request =>
    val data = blastForm.bindFromRequest.get
    val tmpDir = Tool.createTempDirectory("tmpDir")
    val seqFile = new File(tmpDir, "seq.fa")

    //println(TableUtils.EggGene)
    data.method match {
      case "text" =>
        val d = if(!data.queryText.contains(">")){">Query_1\n" + data.queryText}else{data.queryText}
        FileUtils.writeStringToFile(seqFile, d)
      case "file" =>
        val file = request.body.file("file").get
        val datas = FileUtils.readFileToString(file.ref.file)
        val d = if(!datas.contains(">")){">Query_1\n" + datas}else{datas}
        FileUtils.writeStringToFile(seqFile, d)
    }

      val outXml = new File(tmpDir, "out.xml")
      val outHtml = new File(tmpDir, "out.html")
      val outTable = new File(tmpDir, "table.xls")
      val execCommand = new ExecCommand

      val blastType = data.blastType match {
        case "blastn" => "blastn"
        case "blastnGenome" => "blastn"
        case "blastp" => "blastp"
      }

      val db = data.blastType match {
        case "blastn" => "cds/egg"
        case "blastnGenome" => "genome/egg"
        case "blastp" => "pep/egg"
      }


    val seqlist=tmpDir+"/seqlist.txt"

    data.blastType match {
      case "blastnGenome"=> {
        FileUtils.writeLines(new File(seqlist), TableUtils.EggChr.asJava, true)
      }
      case i if i=="blastp" || i=="blastn" =>{
        FileUtils.writeLines(new File(seqlist), TableUtils.EggGene.asJava, true)
      }
    }



    val jinja = blastType
//    val command=
//      s"""|${Utils.path.unixPath}/tools/blastTools/linux-ncbi-blast-2.6.0+/bin/${blastType} -query ${seqFile.unixPath} -db ${Utils.path.unixPath}/blastData/${db} -seqidlist ${seqlist.unixPath} -outfmt 5 -evalue ${data.evalue} -max_target_seqs ${data.maxTargetSeqs} -word_size ${data.wordSize} -out ${outXml.unixPath}
//          |python ${Utils.path.unixPath}/tools/blastTools/blast2html/blast2html.py -i ${outXml.unixPath} -o ${outHtml.unixPath} --template ${Utils.path.unixPath}/tools/blastTools/blast2html/${jinja}.jinja
//          |perl ${Utils.path.unixPath}/tools/blastTools/Blast2table -format 10 -xml ${outXml.unixPath} -header
//          |""".stripMargin
//
//    Utils.orderCallLinuxScript(tmpDir,List(command))

    val command1=Utils.path+"/tools/blastTools/linux-ncbi-blast-2.6.0+/bin/"+blastType+" -query "+seqFile.getAbsolutePath+" -db "+Utils.path+"/blastData/"+db+" -seqidlist "+seqlist+" -outfmt 5 -evalue "+data.evalue+" -max_target_seqs "+data.maxTargetSeqs+" -word_size "+data.wordSize+" -out "+outXml.getAbsolutePath
    val command2 = "python " + Utils.path + "/tools/blastTools/blast2html/blast2html.py -i " + outXml.getAbsolutePath + " -o " +
        outHtml.getAbsolutePath +
        " --template %s/tools/blastTools/blast2html/%s.jinja".format(Utils.path, jinja)
    val command3 = "perl " + Utils.path + "/tools/blastTools/Blast2table -format 10 -xml " + outXml.getAbsolutePath + " -header "

    execCommand.exect(Array(command1,command2,command3),tmpDir)



    if (execCommand.isSuccess) {
        val excel = execCommand.getOutStr
        FileUtils.writeStringToFile(outTable, excel)
        Ok(Json.obj("html" -> tmpDir.getAbsolutePath.replaceAll("\\\\", "/"), "excel" -> excel, "types" -> data.blastType))
      } else {
        Utils.deleteDirectory(tmpDir)
        Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
      }
    }


  def blastResultBefore(path: String, types: String) = Action { implicit request =>
    Ok(views.html.blast.blastResult(path, types))
  }

  def blastResult(path: String,types:String) = Action {
    val html = FileUtils.readFileToString(new File(path + "/out.html"))
    val json = if(types == "blastnGenome"){
      val block = path.getSyntenyData
      Json.obj("html" -> (html + "\n" + Utils.scriptHtml),"block" -> block)
    }else{
      Json.obj("html" -> (html + "\n" + Utils.scriptHtml))
    }
    FileUtils.deleteDirectory(new File(path))
    Ok(json)
  }


  def downByRange(name: String, range: String, blastType: String) = Action {

    val fasta = blastType match {
      case "blastn" => "/blastData/cds/egg.cds"
      case "blastnGenome" => "/blastData/genome/hic.fasta"
      case "blastp" =>  "/blastData/pep/egg.pep"
    }

    val execCommand = new ExecCommand
    range.split("Range").tail.foreach { x =>
      val r = x.trim.split(":").last.split("to").map(_.trim).sortBy(_.toInt)
      val ra = name + ":" + r.mkString("-")

      val command = if (new File(Utils.windowsPath).exists()) {
        Utils.path + "/tools/samtools-0.1.19/samtools.exe faidx " + Utils.path + fasta + " " + ra
      } else {
        "samtools faidx " + Utils.path  + fasta + " " + ra
      }
      println(command)
      execCommand.exec(command)
    }
    val seq = execCommand.getOutStr
    Ok(seq).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + name + "_AlignedSequence.fasta"),
      CONTENT_TYPE -> "application/x-download"
    )

  }

  def downBlast(name: String, blastType: String) = Action {
    val fasta = blastType match {
      case "blastn" => "/blastData/cds/egg.cds"
      case "blastnGenome" => "/blastData/genome/hic.fasta"
      case "blastp" =>  "/blastData/pep/egg.pep"
    }

    val execCommand = new ExecCommand
    val command = if (new File(Utils.windowsPath).exists()) {
      Utils.path + "/tools/samtools-0.1.19/samtools.exe faidx " + Utils.path  + fasta + " " + name
    } else {
      "samtools faidx " + Utils.path  + fasta + " " + name
    }
    println(command)
    execCommand.exec(command)

    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val seqFile = new File(tmpDir + "/seq.fasta")
    execCommand.execo(command, seqFile)
    Ok.sendFile(seqFile).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + name + ".fasta"),
      CONTENT_TYPE -> "application/x-download"
    )

  }




}
