package models

import lib.PlantStates.PlantState

import db.Tables._
import util.SlickDB
import scala.slick.driver.MySQLDriver.simple._

case class Plant(name:String, state:PlantState, picture:String)

object Plant {
  def fromPlantsRow(row:db.Tables.PlantsRow):Plant = SlickDB.withSession { implicit session =>

    val rows = for {
      p <- Plants if p.id === row.id;
      f <- p.filesFk
    } yield (p, f)

    val plantRow = rows.list.head
    Plant(row.name, PlantState(row.currentState), plantRow._2.path)
  }

}
