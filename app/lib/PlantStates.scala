package lib.PlantStates

import scala.util.Random

object PlantState {
  // Maps the state names used in the database to the actual PlantState objects
  val registeredStates:Map[String, PlantState] =
    List(Happy, Okay, Thirsty, Unknown).map(x => (x.toString, x)).toMap

  def apply(name:Option[String]):PlantState = name match {
    case Some(x) => registeredStates(x)
    case _ => Unknown

  }
}

trait PlantState {
  override def toString:String = ???
  def moodStrings:List[String]

  def moodString:String = Random.shuffle(moodStrings).head
}

object Happy extends PlantState {
  override def toString = "happy"
  override def moodStrings: List[String] = List(
//    "Doin' pretty good!",
    "Sooooooo happy"
  )
}

object Okay extends PlantState {
  override def toString = "okay"
  override def moodStrings: List[String] = List(
    "Hmm, water would be nice..."
  )
}

object Thirsty extends PlantState {
  override def toString = "thirsty"
  override def moodStrings: List[String] = List(
    "Call 911, this is a water emergency!"
//    "Help... Me... x_x"
  )
}

object Unknown extends PlantState {
  override def toString = "unknown"
  override def moodStrings: List[String] = List(
//    "Mysterious plant is mysterious!",
//    "I'm afraid I have no data, Dave"
      "No data available yet"
  )
}
