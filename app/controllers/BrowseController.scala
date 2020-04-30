package controllers

import java.io.File

import dao.EggplantDao
import javax.inject._
import models.Tables.EggplantRow
import play.api.data.Form
import play.api.data.Forms.{mapping, number, optional, text}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import utils.{ExecCommand, TableUtils, Utils}

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
case class PageData(limit: Int, offset: Int, order: String, search: Option[String], sort: Option[String])
class BrowseController @Inject()(eggplantDao:EggplantDao, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc){

  val pageForm = Form(
    mapping(
      "limit" -> number,
      "offset" -> number,
      "order" -> text,
      "search" -> optional(text),
      "sort" -> optional(text)
    )(PageData.apply)(PageData.unapply)
  )

  def getSeqsByGeneid(geneid:String)= Action { implicit request =>


      val samtools = if (new File(Utils.windowsPath).exists()) {
        Utils.path + "/tools/samtools-0.1.19/samtools.exe faidx " + Utils.path + "/data/"
      }else {
         "samtools faidx " + Utils.path + "/data/"
      }
    val cds = samtools + "final_sort_qiezi.cds " + geneid+".1"
    val pep = samtools + "final_sort_qiezi.pep " + geneid+".1"

    val exec = new ExecCommand()
    exec.exec(Array(cds,pep))
    val seqs = exec.getOutStr.split(">").tail.map(_.split("\n")).map(x=> ">" + x.head + "\n" + x.tail.mkString)
    Ok(Json.obj("cds" ->  seqs.head,"pep" ->  seqs(1)))
  }

  def getGeneInfo(id: String) = Action.async { implicit request =>
    eggplantDao.getByGeneId(id).map{x=>
      var pfam=""
      var go=""
      x.pfamDes.split(";;").foreach{line=>
        pfam+=line+"\n"
      }
      x.goDes.split(";;").foreach { line =>
        go += line + "\n"
      }

      Ok(views.html.browse.browseInfo(x,Integer.parseInt(x.end)-Integer.parseInt(x.start)+1,pfam,go))
    }
  }


  def getAllEggplant = Action { implicit request =>
    val page = pageForm.bindFromRequest.get
    val x = TableUtils.EggplantMap
    val orderX = TableUtils.dealDataByPage(x, page)
    val total = orderX.size
    val tmpX = orderX.slice(page.offset, page.offset + page.limit)
    val row = tmpX.asInstanceOf[Seq[EggplantRow]].map{x=>
      val geneid = s"<a href='/Eggplant/getGeneInfo?id=${x.geneid}' target='_blank'>" + x.geneid + "</a>"
      val keggid=s"<a href='https://www.kegg.jp/dbget-bin/www_bget?ko:${x.keggNum}' target='_blank'>"+x.keggNum+"</a>"

      val pfamid=splitPfamId(x.pfamNum)
      val goid=splitGoId(x.goNum)
      Json.obj("geneid" -> geneid,"chr"-> x.chr,"start" -> x.start,"end"->x.end,"strand" -> x.strand,
        "kegg" -> keggid,"pfam" -> pfamid,"go"->goid,"nr"->x.nrDes,"swissprot" -> x.swissDes)
    }

    Ok(Json.obj("rows" -> row, "total" -> total))
  }

  def splitPfamId(pfam:String)={
    var keggid=""
    pfam.split(';').foreach{x=>
      keggid+=s"<a href='http://pfam.xfam.org/family/${x}' target='_blank'>"+x+"</a>"+" "
    }
    keggid
  }

  def splitGoId(go:String)={
    var goid=""
    go.split(';').foreach{x=>
      goid+=s"<a href='http://amigo.geneontology.org/amigo/term/${x}' target='_blank'>"+x+"</a>"+" "
    }
    goid
  }





//  def geneInfo(id: String,species:String) = Action.async { implicit request =>
//    speciesdao.getByGeneId(id,species).map { x =>
//      Ok(case "gansi" => views.html.detailInfo.gansiInfo(x.asInstanceOf[Seq[GansiRow]].head))
//    }
//  }

}
