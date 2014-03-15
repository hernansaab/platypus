package server.lib.actionRouters

import akka.actor.{ActorSystem, ActorRef}

/**
 * Created by hernansaab on 3/8/14.
 */
object connectionRouters {
  var system:ActorSystem = null
  var waitConnectionRouter:ActorRef = null
  var workerRouter:ActorRef = null
}
