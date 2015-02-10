package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {

    val plants = (1 to 6).toList
    Ok(views.html.index(plants))

  }

}