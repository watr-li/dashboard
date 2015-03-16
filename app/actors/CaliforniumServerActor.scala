package actors

import actors.messages.PlantHumidityUpdated
import akka.actor.UntypedActor
import coap.CaliforniumServer
import models.Plant
import play.api.Logger

/**
 * Acts as a connector between the Californium Server and the rest of the
 * Play application.
 */
class CaliforniumServerActor extends UntypedActor {
  val logger: Logger = Logger(this.getClass)

  val californiumServer = CaliforniumServer.initialize(self);

  @throws[Exception](classOf[Exception])
  override def onReceive(message: Any): Unit = message match {
    case PlantHumidityUpdated(x) => Plant.updatePlantState(x)
    case _ => logger.info(s"Unhandled message: $message")
  }

  override def postStop():Unit = {
    logger.info("Being shut down!")
    californiumServer.stop()
  }
}
