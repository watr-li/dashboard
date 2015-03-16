package global

import actors.CaliforniumServerActor
import akka.actor.{PoisonPill, Props, ActorRef}
import play.api.{Application, Logger, Play, GlobalSettings}
import play.libs.Akka

object Global extends GlobalSettings {

  val logger: Logger = Logger(this.getClass())

  var californiumActor:Option[ActorRef] = None;

  def uploadDirectory:String = {
    Play.current.configuration.getString("dashboard.uploadDirectory").get
  }

  override def onStart(app:play.api.Application):Unit = {
    logger.info("onStart invoked")
    californiumActor = Some(Akka.system.actorOf(Props[CaliforniumServerActor], name = "CaliforniumServerActor"))
  }

  override def onStop(app:Application):Unit = {
    logger.info("onStop invoked")
    californiumActor match {
      case Some(x) => x ! PoisonPill
      case None => // Do nothing
    }
  }

  def getCaliforniumActor:ActorRef = californiumActor.get

}
