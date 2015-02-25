package coap

import actors.messages.NodeRegistered
import akka.actor.ActorRef
import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.coap.CoAP.ResponseCode
import org.eclipse.californium.core.server.resources.CoapExchange
import play.api.Logger
import util.SlickDB

import scala.slick.driver.MySQLDriver.simple._

/**
 * The Californium Resource which handles registration of nodes in the Internet
 * of Plants.
 *
 *    - PUT /nodes/ registers a new node. The payload must be a unique string
 *                  identifier with a length <= 100 bytes.
 *
 * @param serverActor The actor that is notified on node registration
 */
class NodesResource(serverActor:ActorRef) extends CoapResource("nodes") {
  val logger: Logger = Logger(this.getClass)

  override def handlePUT(ex:CoapExchange):Unit = SlickDB.withSession { implicit session =>
    import db.Tables._

    logger.info(s"Received PUT with Payload ${ex.getRequestText} from ${ex.getSourceAddress}")

    // Store the registered node in the database
    val identifier = ex.getRequestText
    val nodeRow = NodesRow(0, identifier, ex.getSourceAddress.getHostAddress, 5683)
    val q = Nodes returning Nodes.map(_.id)

    q.insertOrUpdate(nodeRow) match {
      case Some(x) => ex.respond(ResponseCode.CREATED)
      case None => ex.respond(ResponseCode.CHANGED)
    }

    val node = Nodes.filter(_.hardwareIdentifier === identifier).list.head
    serverActor ! NodeRegistered(node)
  }
}
