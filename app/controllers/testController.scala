package controllers

import dao.EggplantDao
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class testController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def testBlast = Action {implicit request=>
    Ok(views.html.test.blastTest())
  }



}
