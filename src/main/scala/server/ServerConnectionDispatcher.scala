package server

import akka.actor._
import akka.routing.RoundRobinRouter
import java.io._
import java.net.ServerSocket
import java.net.Socket
import scala.collection.mutable.ListBuffer
import server.lib.{View, MvcRouter, HttpRequest, RequestConnectionFactory}

import net.liftweb.json._
import net.liftweb.json.JsonDSL._


import scala.collection.mutable._
import scala.collection.mutable
import org.fusesource.scalate._
import org.fusesource.scalate.{TemplateEngine, Binding, RenderContext}
import java.io.File
/**
 * Created by hernansaab on 2/26/14.
 */

case class ClientSocketContainer(sock: Socket)
class ServerConnectionDispatcher(actorNumber: Int) extends Actor with ActorLogging {
  def receive: Actor.Receive = {



    case ClientSocketContainer(clientSocket) => {
      val out = new PrintWriter(clientSocket.getOutputStream, true)
      val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))

      var break: Boolean = false;

      def cleanup() = {
        out.flush()
        clientSocket.close()
        in.close()
        out.close()
      }
      var headerCharArray= mutable.DoubleLinkedList('\n')

      try{
        while (!break) {
          val char = in.read
          //double line ends header
          if(char == -1){
            break = true
          }else{
            headerCharArray.append(mutable.DoubleLinkedList(char.toChar))
          }

          var rev = headerCharArray.reverseIterator

          var current:Int = '\0'
          var prev:Int = '\0'
          var prevPrev:Int  = '\0'
          var prevPrevPrev:Int = '\0'
          if(headerCharArray.size >= 4 ) {
            val(current, prev, prevPrev, prevPrevPrev) = (rev.next, rev.next, rev.next, rev.next)
            if( current == '\n' && prev == '\r' && prevPrev == '\n' && prevPrevPrev == '\r'){
              break = true
            }
          }

        }
        headerCharArray = headerCharArray.drop(1)
        val header =
          if (headerCharArray.size != 0)
            headerCharArray.mkString("")
          else ""
        break = true
        var bodyCharArray= new ListBuffer[Char]()
        val request = RequestConnectionFactory.generateRequestConnection(header, in, out, cleanup)
        if (request.postSize != 0) {
          var postSizeAcc:Long = 0;
          while (!break) {
            val char = in.read()
            bodyCharArray += char.toChar
            postSizeAcc += 1

            if (postSizeAcc >= request.postSize || char == -1) {
              break = true
            }
          }
        }
        request.body =
          if (bodyCharArray.size != 0)
            bodyCharArray.mkString("")
          else ""
        println("---generate router-----")
        MvcRouter.route(request)

    }
      catch {
        case e: IOException => log.error(("something went wrong---" + e.getMessage).+("\n---")+e.getStackTrace)
      }
    }
    case msg => log.debug(s"Message from  actor $actorNumber: $msg")
  }
}

object ServerConnectionDispatcher {
  def apply(param: Int): Props = Props(new ServerConnectionDispatcher(param))
}

object Main extends App {
  val system = ActorSystem()
  val actors = new ListBuffer[ActorRef]
  for (i <- 1 to Configuration.workers) {
    actors += system.actorOf(ServerConnectionDispatcher(i))
  }

  View.startScalate()

  val routerProps = Props.empty.withRouter(RoundRobinRouter(actors))
  val actorRouter = system.actorOf(routerProps, "round-robin")
  var serverSocket = new ServerSocket(Configuration.port)

  while(true){
    val clientSocket = serverSocket.accept;
    actorRouter ! ClientSocketContainer(clientSocket)
  }

  system.shutdown()

}