package server.lib

import org.fusesource.scalate.TemplateEngine
import akka.actor.{Props, ActorRef, ActorSystem}
import scala.collection.mutable.ListBuffer
import server.{ServerConnectionDispatcher, Configuration}
import akka.routing.RoundRobinRouter

/**
 * Created by hernansaab on 3/7/14.
 */
object Booter {


  def start():Boolean = {

    //boot up template engine
    View.engine = new TemplateEngine()

    //boot up template engine
    actionRouters.connectionRouters.system = ActorSystem()
    val actors = new ListBuffer[ActorRef]
    for (i <- 1 to Configuration.workers) {
      actors += actionRouters.connectionRouters.system .actorOf(ServerConnectionDispatcher(i))
    }
    val routerProps = Props.empty.withRouter(RoundRobinRouter(actors))
    actionRouters.connectionRouters.router = actionRouters.connectionRouters.system .actorOf(routerProps, "round-robin")

    true
  }
}
