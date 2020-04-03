package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

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

  /*
  def download=Action{implicit request=>
    Ok(views.html.download.download())  //跳转下载页
  }

*/



}
