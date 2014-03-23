package server.lib.actionRouters

import akka.actor.{ActorSystem, ActorRef}
import com.lmax.disruptor.RingBuffer
import server.lib.HttpRequest
import server.ValueEvent
;

/**
 * Created by hernansaab on 3/8/14.
 */
object connectionRouters {
  var system:ActorSystem = null
  var readerRouter:ActorRef = null
  var workers:ActorRef = null
 var ringBuffer:RingBuffer[ValueEvent] = null
}
