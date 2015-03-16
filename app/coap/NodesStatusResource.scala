package coap

import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.coap.CoAP.ResponseCode
import org.eclipse.californium.core.server.resources.CoapExchange
import play.api.Logger

/**
 * Receives and processes humidity value updates from a node. Each node
 * sends updates to the following path:
 *
 *    /nodes/[node hardware identifier]/status
 *
 * The payload is the humidity value. The parent resource, i.e. the one
 * with the node's hardware identifier as name, is created in the
 * NodesResource
 *
 * @param nodeId Database ID of the node to be updated when a status arrives
 */
class NodesStatusResource(nodeId:Int) extends CoapResource("status") {
  val logger: Logger = Logger(this.getClass)

  override def handlePUT(ex:CoapExchange):Unit = {
    logger.info(s"Received PUT with Payload ${ex.getRequestText} from ${ex.getSourceAddress}")
    ex.respond(ResponseCode.CHANGED)
  }
}
