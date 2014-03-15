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
   // for (i <- 1 to Configuration.inputRouterCount()) {
    //  //context.actorOf(Props[MyActor].withDispatcher("my-dispatcher"), "myactor1")
    //  actors += actionRouters.connectionRouters.system .actorOf(Props(new ServerConnectionDispatcher(i)).withDispatcher("akka.actor.deployment.my-dispatcher"))
   // }

  //  val routerProps = Props.empty.withRouter(RoundRobinRouter(routees=actors))
  //  actionRouters.connectionRouters.readConnectionRouter = actionRouters.connectionRouters.system.actorOf(routerProps, "round-robin")

    //actionRouters.connectionRouters.readConnectionRouter = actionRouters.connectionRouters.system.actorOf((Props[ServerConnectionDispatcher]), "router1")
    actionRouters.connectionRouters.waitConnectionRouter = actionRouters.connectionRouters.system.actorOf(FromConfig.props(Props[ServerConnectionDispatcher]), "router1")
    actionRouters.connectionRouters.workerRouter = actionRouters.connectionRouters.system.actorOf(FromConfig.props(Props[ServerConnectionDispatcher]), "router2")

   //   actionRouters.connectionRouters.readConnectionRouter = actors(0)


      //    actionRouters.connectionRouters.system = ActorSystem()

    //val actors2 = new ListBuffer[ActorRef]

   // for (i <- 1 to 1){// Configuration.outputRouterCount()) {
    //  actors2 += actionRouters.connectionRouters.system .actorOf(ServerConnectionDispatcher(i))
   // }
    //val routerProps2 = Props.empty.withRouter(RoundRobinRouter(routees=actors2))

    //actionRouters.connectionRouters.writeConnectionRouter = actionRouters.connectionRouters.system .actorOf(routerProps2, "round-robin2")
   // actionRouters.connectionRouters.readConnectionRouter = actors2(0)


    true
  }
}
