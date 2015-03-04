package controllers

import actors.WebSocketActor
import play.api.mvc._

import play.api.Play.current

object Application extends Controller {

  def handleWebsocketConnection = WebSocket.acceptWithActor[String ,String] { request => out =>
    WebSocketActor.props(out)
  }

}
