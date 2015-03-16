package actors.messages

import db.Tables._

/**
 * Indicates that a new hardware node has joined the Internet of Plants
 */
case class NodeRegistered(node:NodesRow)

/**
 * Triggered when a hardware node posts and updated humidity value
 */
case class PlantHumidityUpdated(plant:PlantHumiditiesRow)

class WebSocketSpawned
