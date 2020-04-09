package controllers

import javax.inject._
import play.api.mvc._


@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {



  def index = Action {implicit request=>
    Redirect(routes.HomeController.home())
  }

  def home=Action{implicit request=>
    Ok(views.html.home.index())  //跳转主页
  }

  def about=Action{implicit request=>
    Ok(views.html.about.about())  //跳转主页
  }

  def download=Action{implicit request=>
    Ok(views.html.download.download()) //跳转下载页面
  }

  def browse=Action{implicit request=>
    Ok(views.html.browse.browse()) //跳转浏览页面
  }

  def browseInfo=Action{implicit  request=>
    Ok(views.html.browse.browseInfo())

  }

  /*
  def download=Action{implicit request=>
    Ok(views.html.download.download())  //跳转下载页
  }

*/



}
