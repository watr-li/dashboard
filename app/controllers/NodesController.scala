package controllers

import play.api.mvc._
import util.SlickDB

import db.Tables._
import scala.slick.driver.MySQLDriver.simple._

object NodesController extends Controller {

  def index = Action {
    SlickDB.withSession { implicit session =>


      Ok(views.html.node.index(Nodes.list))

    }
  }

}
