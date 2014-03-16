package server

import akka.actor._
import akka.routing.RoundRobinRouter
import java.io._
import java.net.ServerSocket
import java.net.Socket
import scala.collection.mutable.ListBuffer
import lib._




import scala.collection.mutable._
import scala.collection.mutable
import org.fusesource.scalate._
import org.fusesource.scalate.{TemplateEngine, Binding, RenderContext}
import java.io.File
import java.util.logging.{Level, Logger}
import java.lang.management.ManagementFactory
import scala.reflect.io.File
import akka.dispatch.Dispatchers
import scala.concurrent.duration._
import akka.event._
/**
 * Created by hernansaab on 2/26/14.
 */


class ServerConnectionDispatcher() extends Actor with ActorLogging {

  import context.dispatcher

  val logger:akka.event.LoggingAdapter = Helpers.logger(context.system,self.getClass.toString)

  logger.log(akka.event.Logging.LogLevel(1), "constructing stuff")
  def receive: Actor.Receive = {

    case server.TransactionConnectionContainerReader(request) => {
      try {
        readRequest(request)
      }
      catch {
        case e: Throwable => logger.log(akka.event.Logging.LogLevel(1), ("Connection possibly closed by client---" + e.getMessage).+("\n---"))
          if (request != null) {
            request.addTransaction(new SingleTransaction(null))
          }
          request.cleanup()
      }
    }

    case server.ConnectionReadyWaiter(request) =>{
      listenConnection(request)
    }
    case server.ClientSocketContainer(clientSocket, ts) => {
      logger.log(akka.event.Logging.LogLevel(1), "INTERCEPT -------------------Process delay  for TS" + ts / 1000000 +
        "------->" + (System.nanoTime() - ts) / 1000000)

      val out = new PrintWriter(clientSocket.getOutputStream, true)
      val stream = new InputStreamReader(clientSocket.getInputStream)
      val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
      def cleanup() = {
        try {
          out.flush()

        } catch {
          case e: Exception => logger.log(akka.event.Logging.LogLevel(1), "Connection possibly timed out before we close it--1-" + e.getMessage + ("\n---") + e.getStackTrace)
        }

        try {
          in.close()
        } catch {
          case e: Exception => logger.log(akka.event.Logging.LogLevel(1), ("Connection possibly timed out before we close it--3-" + e.getMessage).+("\n---") + e.getStackTrace)
        }
        try {
          out.close()
        } catch {
          case e: Exception => logger.log(akka.event.Logging.LogLevel(1), ("Connection possibly timed out before we close it--4-" + e.getMessage).+("\n---") + e.getStackTrace)
        }
        try {
          clientSocket.close()

        } catch {
          case e: Exception => logger.log(akka.event.Logging.LogLevel(1), ("Connection possibly timed out before we close it--2-" + e.getMessage + ("\n---") + e.getStackTrace))
        }
      }
      val request = RequestConnectionFactory.generateRequestConnection(in, out, ts, stream, cleanup)
      var success = listenConnection(request)
      if(success){
        lib.actionRouters.connectionRouters.workerRouter ! server.TransactionConnectionContainerWriter(request)

      }
    }


    case server.TransactionConnectionContainerWriter(request) => {
      val st = System.nanoTime()
      val status = ServerRouter.route(request)
      if (status == 2) {
        logger.log(akka.event.Logging.LogLevel(1), "Process delay  for TS--" + ((System.nanoTime() - request.startTime) / 1000000) +
          "-->" + ((System.nanoTime() - request.startTime) / 1000000)+"---and route delay is ---- "+ (System.nanoTime()-st)/1000000)
      }
      if (status != 0) {

        if(status == 2){
          try {
            lib.actionRouters.connectionRouters.workerRouter ! new server.TransactionConnectionContainerWriter(request.copy)

          } catch {
            case e: Throwable => logger.log(akka.event.Logging.LogLevel(1), s"Message from  actor---------------------- route exception----" + e.getMessage + "-----stack:" + e.getStackTraceString)
          }
        }else{
          context.system.scheduler.scheduleOnce(2 milliseconds) {
            try {
              lib.actionRouters.connectionRouters.workerRouter ! new server.TransactionConnectionContainerWriter(request.copy)

            } catch {
              case e: Throwable => logger.log(akka.event.Logging.LogLevel(1), s"Message from  actor---------------------- route exception----" + e.getMessage + "-----stack:" + e.getStackTraceString)
            }
          }
        }
      }
    }

    case _ => {
      logger.log(akka.event.Logging.LogLevel(1), "------------------woa--------- errr receiving")
    }
  }

  def listenConnection(request:HttpRequest):Boolean = {
    try {
      if(!request.in.ready()){
        context.system.scheduler.scheduleOnce(2 milliseconds) {
          lib.actionRouters.connectionRouters.waitConnectionRouter ! server.ConnectionReadyWaiter(request.copy)
        }

        return true
      }
    }catch {
      case e: Throwable => logger.log(akka.event.Logging.LogLevel(1), ("Initial reading Connection possibly closed by client---" + e.getMessage) + ":"+ e.getStackTraceString + ("\n---"))
        if (request != null) {
          request.addTransaction(new SingleTransaction(null))
        }
        request.cleanup()
      return false
    }
    println("going to write!!--------------------")
    lib.actionRouters.connectionRouters.workerRouter ! server.TransactionConnectionContainerReader(request.copy)
    return true
  }





  def readRequest(request: HttpRequest) {
    var header: String = ""
    var transaction: SingleTransaction = null

    try {
      header = readHeader(request.in)
      if (header == "") {
        transaction = new SingleTransaction(null)

      } else {
        transaction = new SingleTransaction(header)
        transaction.body = readBody(request, request.in, transaction.postSize)
      }

    } catch {
      case e: Throwable => {

        header = ""
        transaction = new SingleTransaction(null)

        request.cleanup()
      }

    }
    request.addTransaction(transaction)



    if (header != "" && transaction.connectionType != "close") {
      try {
        lib.actionRouters.connectionRouters.waitConnectionRouter ! new server.ConnectionReadyWaiter(request.copy)

      } catch {
        case e: Throwable => logger.log(akka.event.Logging.LogLevel(1), s"Message from  actor---------------------- route exception----" + e.getMessage + "-----stack:" + e.getStackTraceString)
      }
    }
  }

  def readBody(request: HttpRequest, in: BufferedReader, size: Int): String = {
    var break2 = true
    var bodyCharArray = new ListBuffer[Char]()

    if (size != 0) {
      var postSizeAcc: Long = 0;
      while (!break2) {
        val char = in.read()
        bodyCharArray += char.toChar
        postSizeAcc += 1
        if (char == -1) {
          return ""
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


object Main extends App {
  val system: ActorSystem = ActorSystem.create();
  //println("----------------------"+system.settings);
  private val log = Helpers.logger(getClass.toString)
  println("FROM SERVER: platypus is starting2")
  /*
    if(!server.isPortAvailable(Configuration.port)){
      println("FROM SERVER: platypus is starting3")
      log.log(Level.SEVERE, "Port number "+Configuration.port+" is already being used")
      System.exit(1)
    }
    */

  var processName = ManagementFactory.getRuntimeMXBean().getName


  scala.reflect.io.File("PID").writeAll(ManagementFactory.getRuntimeMXBean().getName().split("@")(0))


  lib.Booter.start()
  var serverSocket: ServerSocket = null


  if (server.isPortAvailable(Configuration.port)) {
    serverSocket = new ServerSocket(Configuration.port)

  } else {
    log.log(Level.SEVERE, "Port number " + Configuration.port + " is already being used.\n")

  }
  var i = 0;
  while (true) {
    val clientSocket = serverSocket.accept;
    clientSocket.setSoTimeout(Configuration.timeoutMilliseconds)
    log.log(Level.INFO, "----------------creating connection----"+i)
    lib.actionRouters.connectionRouters.waitConnectionRouter ! server.ClientSocketContainer(clientSocket, System.nanoTime())
    i += 1
  }

  lib.actionRouters.connectionRouters.system.shutdown()

}