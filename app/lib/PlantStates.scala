package lib.PlantStates

import scala.util.Random

trait PlantState {
  override def toString:String = ???
  def moodStrings:List[String]

  def moodString:String = Random.shuffle(moodStrings).head
}

object Happy extends PlantState {
  override def toString = "happy"
  override def moodStrings: List[String] = List(
    "Doin' pretty good!",
    "Sooooooo happy =)"
  )
}

object Okay extends PlantState {
  override def toString = "okay"
  override def moodStrings: List[String] = List(
    "Hmm, water would be nice :s"
  )
}

object Thirsty extends PlantState {
  override def toString = "thirsty"
  override def moodStrings: List[String] = List(
    "Call 911, this is a water emergency!",
    "Help... Me... x_x"
  )
}