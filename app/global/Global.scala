package global

import play.api.{Play, GlobalSettings}

object Global extends GlobalSettings {

  def uploadDirectory:String = {
    Play.current.configuration.getString("dashboard.uploadDirectory").get
  }
  
}
