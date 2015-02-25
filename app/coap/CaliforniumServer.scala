package coap

import akka.actor.ActorRef
import org.eclipse.californium.core.CoapServer
import play.api.Logger

/**
 * @param serverActor Reference to the actor that will be notified if a CoAP message is received.
 */
class CaliforniumServer(serverActor:ActorRef) extends CoapServer {
  val logger: Logger = Logger(this.getClass)

  logger.info("Initializing.")
  add(new NodesResource(serverActor))
}

object CaliforniumServer {

  def initialize(serverActor:ActorRef):CaliforniumServer = {
    val server = new CaliforniumServer(serverActor)
    server.start()
    server
  }

}
