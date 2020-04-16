package controllers

import java.io.{File, PrintWriter}

import dao.EggplantDao
import javax.inject.Inject
import models.Tables.EggplantRow
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.FilesUtils

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class InsertController @Inject()(eggplantDao:EggplantDao, cc: ControllerComponents) extends AbstractController(cc) {

  def insert = Action{

    val row=FilesUtils.joinAll.map{ x=>
      EggplantRow(0,x.head,x(1),x(2),x(3),x(4),x(5),x(6),x(7),x(8),x(9),x(10),x(11),x(12))
    }
    Await.result(eggplantDao.insert(row),Duration.Inf)

    Ok(Json.toJson(1))

  }





}
