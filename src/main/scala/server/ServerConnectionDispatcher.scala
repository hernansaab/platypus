package server

import akka.actor._
import akka.routing.RoundRobinRouter
import java.io._
import java.net.ServerSocket
import java.net.Socket
import scala.collection.mutable.ListBuffer
import lib._

import net.liftweb.json._
import net.liftweb.json.JsonDSL._


import scala.collection.mutable._
import scala.collection.mutable
import org.fusesource.scalate._
import org.fusesource.scalate.{TemplateEngine, Binding, RenderContext}
import java.io.File
import java.util.logging.{Level, Logger}
import java.lang.management.ManagementFactory
import scala.reflect.io.File

/**
 * Created by hernansaab on 2/26/14.
 */


class ServerConnectionDispatcher(actorNumber: Int) extends Actor with ActorLogging {
  def receive: Actor.Receive = {



    case server.ClientSocketContainer(clientSocket) => {
      val out = new PrintWriter(clientSocket.getOutputStream, true)
      val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
      def cleanup() = {
        try{
          out.flush()
          clientSocket.close()
          in.close()
          out.close()
        }catch{
          case e: Exception => log.warning(("Connection possibly timed out before we close it---" + e.getMessage).+("\n---") + e.getStackTrace)
        }

      }

      runConnection(out, cleanup, in)
    }

    case server.TransactionConnectionContainer(request) => {
      ServerRouter.route(request)
    }

    case msg => log.debug(s"Message from  actor $actorNumber: $msg")
  }

  def runConnection(out: Writer, cleanup:()=>Unit, in: BufferedReader) {


    try {//work on timeout live


      val request = RequestConnectionFactory.generateRequestConnection(in, out, cleanup)
      lib.actionRouters.connectionRouters.router ! server.TransactionConnectionContainer(request)
      var header:String = ""
      var transaction:SingleTransaction = null
      do{
        try{
          header  = readHeader(in)
          transaction = new SingleTransaction(header)

          transaction.body = readBody(request, in, transaction.postSize)

        }catch {
          case e:Throwable => {
            header = ""
            transaction = new SingleTransaction(null)
            cleanup()
          }

        }

        request.addTransaction(transaction)
      }while(header != ""  && transaction.connectionType != "close")

    }
    catch {
      case e: IOException => log.debug(("Connection possibly closed by client---" + e.getMessage).+("\n---"))
    }

  }


  def readBody(request: HttpRequest, in: BufferedReader, size:Int):String = {
    var break2 = true
    var bodyCharArray = new ListBuffer[Char]()

    if (size != 0) {
      var postSizeAcc: Long = 0;
      while (!break2) {
        val char = in.read()
        bodyCharArray += char.toChar
        postSizeAcc += 1
        if (char == -1) {
          return null
        }
        if (postSizeAcc >= size) {
          break2 = true
        }
      }
    }

      if (bodyCharArray.size != 0)
        bodyCharArray.mkString("")
      else ""
  }

  def readHeader(in: BufferedReader): String = {
    var headerCharArray = mutable.DoubleLinkedList('\n')
    var break: Boolean = false;
    while (!break) {
      val char = in.read
      //double line ends header
      if (char == -1) {
        return ""
      } else {
        headerCharArray.append(mutable.DoubleLinkedList(char.toChar))
      }

      var rev = headerCharArray.reverseIterator

      var current: Int = '\0'
      var prev: Int = '\0'
      var prevPrev: Int = '\0'
      var prevPrevPrev: Int = '\0'
      if (headerCharArray.size >= 4) {
        val (current, prev, prevPrev, prevPrevPrev) = (rev.next, rev.next, rev.next, rev.next)
        if (current == '\n' && prev == '\r' && prevPrev == '\n' && prevPrevPrev == '\r') {
          break = true
        }
      }

    }
    headerCharArray = headerCharArray.drop(1)
    val header =
      if (headerCharArray.size != 0)
        headerCharArray.mkString("")
      else ""
    header
  }
}

object ServerConnectionDispatcher {
  def apply(param: Int): Props = Props(new ServerConnectionDispatcher(param))
}

object Main extends App {

  private val log = Logger.getLogger(getClass.toString)
  println("FROM SERVER: platypus is starting2")
/*
  if(!server.isPortAvailable(Configuration.port)){
    println("FROM SERVER: platypus is starting3")
    log.log(Level.SEVERE, "Port number "+Configuration.port+" is already being used")
    System.exit(1)
  }
  */
  var processName = ManagementFactory.getRuntimeMXBean().getName

  println("FROM SERVER: platypus is starting and process is "+ processName)

  println("process id ----"+ManagementFactory.getRuntimeMXBean().getName().split("@")(0))
  scala.reflect.io.File("PID").writeAll(ManagementFactory.getRuntimeMXBean().getName().split("@")(0))


  lib.Booter.start()
  var serverSocket = false
  try{
    var serverSocket = new ServerSocket(Configuration.port)

  }catch {
    case e:Throwable =>{
      log.log(Level.SEVERE, "Port number "+Configuration.port+" is already being used.\n"+e.getStackTraceString)
      System.exit(3)
    }
  }
  while(true){
    val clientSocket = serverSocket.accept;
    clientSocket.setSoTimeout(Configuration.timeoutMilliseconds)

    lib.actionRouters.connectionRouters.router ! server.ClientSocketContainer(clientSocket)
  }

  lib.actionRouters.connectionRouters.system.shutdown()

}