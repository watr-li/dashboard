package controllers

import actors.WebSocketActor
import com.fasterxml.jackson.annotation.JsonValue
import play.api.libs.json.JsValue
import play.api.mvc._

import play.api.Play.current

import play.api.libs.json._



object Application extends Controller {


  def handleWebsocketConnection = WebSocket.acceptWithActor[String, String] { request => out =>
    WebSocketActor.props(out)
  }

}
