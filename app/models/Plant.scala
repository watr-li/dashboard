package models

import lib.{JToot, PlantStates}

import db.Tables._
import lib.PlantStates.PlantState
import play.api.Logger
import util.SlickDB
import scala.slick.driver.MySQLDriver.simple._

case class Plant(name:String, state:PlantState, picture:String)

object Plant {
  val logger: Logger = Logger(this.getClass)

  def fromPlantsRow(row:db.Tables.PlantsRow):Plant = SlickDB.withSession { implicit session =>

    val rows = for {
      p <- Plants if p.id === row.id;
      f <- p.filesFk
    } yield (p, f)

    val plantRow = rows.list.head
    Plant(row.name, PlantState(row.currentState), plantRow._2.path)
  }

  def updatePlantState(humidityRow:PlantHumiditiesRow):Unit = SlickDB.withSession { implicit session =>
    // TODO: Proper humidity boundaries
    val state = humidityRow.humidity match {
      case x if x < 1000 => PlantStates.Thirsty
      case x if x < 2500 => PlantStates.Okay
      case _ => PlantStates.Happy
    }

    logger.info(s"Updating plant state of plant ${humidityRow.plantId} to $state")
    val plant = for(p <- Plants if p.id === humidityRow.plantId) yield p


    plant.list.headOption match {
      case Some(row) => if(row.currentState.getOrElse("") != state.toString) {
        try {
          new JToot().toot(s"@watr_li Status of '${row.name}' changed to ${state.toString}!")
          } catch {
           case e:Exception =>
            println(e.getMessage())
            println(e.getStackTrace())
          }
      }
      case _ => /* nothing */
    }

    val plantState = for(p <- Plants if p.id === humidityRow.plantId) yield p.currentState
    plantState.update(Some(state.toString))
  }

}
