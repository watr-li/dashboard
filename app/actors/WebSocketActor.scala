package actors

import akka.actor.{ActorRef, Props, UntypedActor}
import play.api.Logger

/**
 * Created by lucas on 04/03/15.
 */
class WebSocketActor extends UntypedActor {
  val logger: Logger = Logger(this.getClass)


  @throws[Exception](classOf[Exception])
  override def onReceive(message: Any): Unit = message match {
    case x => logger.debug(s"Received message: $x")
  }
}

object WebSocketActor {
  def props(out:ActorRef) = Props(new WebSocketActor)
}
