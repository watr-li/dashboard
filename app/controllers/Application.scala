package controllers

import play.api.mvc._

import lib.PlantStates._

import scala.util.Random


case class Plant(name:String, state:PlantState, avatar:String) {

}

object Application extends Controller {

  def index = Action {

    val plants = (1 to 6).map { i =>
      val state = Random.shuffle(List(Happy, Okay, Thirsty)).head
      val avatar = Random.shuffle(List("plant1.jpg", "plant2.jpg")).head
      Plant("Fooplant", state, avatar)
    }
    //val plants = (1 to 6).toList
    Ok(views.html.index(plants))

  }

}