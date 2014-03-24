package server.lib

import org.fusesource.scalate.TemplateEngine
import akka.actor.{Actor, Props, ActorRef, ActorSystem}
import scala.collection.mutable.ListBuffer
import server.{Configuration}
import akka.routing.{FromConfig, RoundRobinRouter}
import java.net.InetSocketAddress
import server._
/**
 * Created by hernansaab on 3/7/14.
 */

import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing._
import akka.io.{Tcp, IO}
import Tcp._
import akka.routing.RoundRobinRoutingLogic
object Booter {



  def start():Boolean = {

    //boot up template engine
    View.engine = new TemplateEngine()

    //boot up template engine
    actionRouters.connectionRouters.system = ActorSystem()
    val actors = new ListBuffer[ActorRef]

    actionRouters.connectionRouters.workers = actionRouters.connectionRouters.system.actorOf(FromConfig.props(Props[ServerConnectionDispatcher]), "transactor")




    true
  }
}
