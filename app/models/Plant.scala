package models

/**
 * Created by lucas on 15/02/15.
 */

import lib.PlantStates.PlantState
import play.api.db._



case class Plant(name:String, state:PlantState, avatar:String) {

}