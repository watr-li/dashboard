package coap

import actors.messages.PlantHumidityUpdated
import akka.actor.ActorRef
import org.eclipse.californium.core.CoapResource
import org.eclipse.californium.core.coap.CoAP.ResponseCode
import org.eclipse.californium.core.server.resources.CoapExchange
import play.api.Logger

import db.Tables._
import util.SlickDB
import scala.slick.driver.MySQLDriver.simple._

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
class NodesStatusResource(nodeId:Int, serverActor:ActorRef) extends CoapResource("status") {
  val logger: Logger = Logger(this.getClass)

  override def handlePUT(ex:CoapExchange):Unit = {
    logger.info(s"Received PUT with Payload ${ex.getRequestText} from ${ex.getSourceAddress}")

    val plant = SlickDB.withSession { implicit session =>
      val plants = for {
        (n, p) <- Nodes leftJoin Plants on (_.plantId === _.id) if n.id === nodeId
      } yield p.?

      plants.list.head
    }

    plant match {
      case None =>
        logger.info(s"Did not find plant for node with id $nodeId")
        ex.respond(ResponseCode.NOT_ACCEPTABLE)

      case Some(plant) =>
        logger.info(s"Found plant with id ${plant.id} for node with id $nodeId")
        SlickDB.withSession { implicit session =>
          val newRow = PlantHumiditiesRow(0, plant.id, ex.getRequestText.toInt)
          PlantHumidities += PlantHumiditiesRow(0, plant.id, ex.getRequestText.toInt)
          serverActor ! PlantHumidityUpdated(newRow)
        }
        ex.respond(ResponseCode.CHANGED)
    }




  }
}
