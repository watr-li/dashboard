package actors.messages

import db.Tables.NodesRow

/**
 * Indicates that a new hardware node has joined the Internet of Plants
 */
case class NodeRegistered(node:NodesRow)
