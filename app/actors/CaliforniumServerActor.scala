package actors

import actors.messages.{WebSocketSpawned, PlantHumidityUpdated}
import akka.actor.{ActorRef, UntypedActor}
import coap.CaliforniumServer
import models.Plant
import play.api.Logger

import scala.collection.mutable

/**
 * Acts as a connector between the Californium Server and the rest of the
 * Play application.
 */
class CaliforniumServerActor extends UntypedActor {
  val logger: Logger = Logger(this.getClass)

  val californiumServer = CaliforniumServer.initialize(self)

  var webSocketActors:List[ActorRef] = Nil

  @throws[Exception](classOf[Exception])
  override def onReceive(message: Any): Unit = message match {
    case PlantHumidityUpdated(x) =>
      Plant.updatePlantState(x)
      // Forward message to websocket actor such that the UI may be updated
      webSocketActors.foreach( _ ! message)

    case x:WebSocketSpawned => webSocketActors = sender :: webSocketActors
    case _ => logger.info(s"Unhandled message: $message")
  }

  override def postStop():Unit = {
    logger.info("Being shut down!")
    californiumServer.stop()
  }
}
