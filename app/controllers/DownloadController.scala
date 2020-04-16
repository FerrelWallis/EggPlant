package controllers

import javax.inject._
import play.api.mvc._
import java.awt.Desktop.Action
import java.io.File
import java.nio.file.Files

import play.api.libs.json.Json
import utils.Utils

import scala.concurrent.ExecutionContext



@Singleton
class DownloadController @Inject()(cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def download(name:String) = Action{implicit request=>

    val file=new File(Utils.path+"/download/"+name)
    Ok.sendFile(file).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + file.getName()),
      CONTENT_TYPE -> "application/x-download"
    )

  }

  def getFiles= Action{implicit request=>
    val files = new File(Utils.path+"/download").listFiles().map(_.getName)
    Ok(Json.toJson(files))
  }



}
