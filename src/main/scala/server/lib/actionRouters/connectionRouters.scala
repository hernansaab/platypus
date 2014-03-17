package server.lib.actionRouters

import akka.actor.{ActorSystem, ActorRef}

/**
 * Created by hernansaab on 3/8/14.
 */
object connectionRouters {
  var system:ActorSystem = null
  var readerRouter:ActorRef = null
  var writerRouter:ActorRef = null
}
