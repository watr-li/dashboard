package controllers

import models.Plant
import play.api.mvc._
import util.SlickDB
import db.Tables._
import scala.slick.driver.MySQLDriver.simple._

object Application extends Controller {

  def index = Action {
    SlickDB.withSession { implicit session =>
      val plants = Plants.list.map(Plant.fromPlantsRow)
      Ok(views.html.index(plants))
    }
  }

}
