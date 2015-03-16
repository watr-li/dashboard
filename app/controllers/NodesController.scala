package controllers

import play.api.mvc._
import util.SlickDB

import db.Tables._
import scala.slick.driver.MySQLDriver.simple._

object NodesController extends Controller {

  def index = Action {
    SlickDB.withSession { implicit session =>

//      val plantsWithNodes = for {
//        (p, n) <- Plants leftJoin Nodes on (_.id === _.plantId)
//      } yield (p, n.id.?)
//
//      val unassignedPlants = plantsWithNodes.list.filter(!_._2.isDefined).map(_._1)
      Ok(views.html.node.index(Nodes.list, Plants.list))
    }
  }

}
