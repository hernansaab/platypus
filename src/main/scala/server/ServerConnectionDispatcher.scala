package server

import akka.actor._
import akka.routing.RoundRobinRouter
import java.io._
import java.net.{SocketException, ServerSocket, Socket}
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
import java.util.concurrent.LinkedBlockingQueue
import scala.util.control.Breaks._

/**
 * Created by hernansaab on 2/26/14.
 */


class ServerConnectionDispatcher() extends Actor with ActorLogging {

  import context.dispatcher

  val logger: akka.event.LoggingAdapter = Helpers.logger(context.system, self.getClass.toString)

  logger.log(akka.event.Logging.LogLevel(2), "constructing stuff")

  def delayNanoseconds(delay:Int) = {
    val t = System.nanoTime()
    while( (System.nanoTime() - t) < delay){
      ////
    }
  }

  def receive: Actor.Receive = {

    case server.Fire(worker, workersQueue) => {
      logger.log(akka.event.Logging.LogLevel(3), "----------start of receiver queue -----------" + workersQueue)

      while (true) {
        breakable {
          val request = workersQueue.poll()
          if (request == null) break()

          if(  (System.nanoTime() - request.lastRead)/1000000 > Configuration.timeoutMilliseconds){
            request.cleanup()
            break()
          }
          var success = true

          var ready = false
          try {
            ready = request.in.ready()
          } catch {
            case e: Throwable =>
              logger.log(akka.event.Logging.LogLevel(2), ("Connection  closed by cli timeout??"))
              request.cleanup()
              break()
          }

          if (ready) {
            try {
              success = readRequest(request)
            }
            catch {
              case e: Throwable => logger.log(akka.event.Logging.LogLevel(3), ("Connection possibly closed by client---" + e.getMessage).+("\n---"))
                request.addTransaction(new SingleTransaction(null))
                request.cleanup()
                success = false
            }
          } else {
            workersQueue.add(request)
            break()
          }
          if (!success) break
          var writeStatus = 1;
          writeStatus = ServerRouter.route(request)
          if (writeStatus == 2) {
            //     logger.log(akka.event.Logging.LogLevel(3), "Total delay--" + ((ts3 - ts1) / 1000) +
            //          "--read delay-->" + ((ts2 - ts1) / 1000) + "---and route delay is ---- " + (ts3 - ts2) / 1000)
          }
          if (writeStatus == 0 || !success) {
            request.cleanup()
            break
          }
          workersQueue.offer(request)
        }
      }
    }
    case _ => {
      logger.log(akka.event.Logging.LogLevel(3), "------------------woa--------- errr receiving")
    }
  }


  def readRequest(request: HttpRequest): Boolean = {
    var header: String = ""
    var transaction: SingleTransaction = null

    try {
      header = readHeader(request.in)

      request.lastRead = System.nanoTime()
      if (header == "") {
        transaction = new SingleTransaction(null)
        request.addTransaction(transaction)
        return false

      } else {
        transaction = new SingleTransaction(header)

        transaction.body = readBody(request, request.in, transaction.postSize)
        request.lastRead = System.nanoTime()
      }

    } catch {
      case e: Throwable => {
        logger.log(akka.event.Logging.LogLevel(2), ("Cssssssonnection  closed by cli timeout??"))

        header = ""
        transaction = new SingleTransaction(null)

        request.cleanup()
        request.addTransaction(transaction)
        return false
      }

    }
    request.addTransaction(transaction)

    if (header != "" && transaction.connectionType != "close") {
      return true
    }
    return false
  }

  def readBody(request: HttpRequest, in: Reader, size: Int): String = {
    var break2 = true
    var bodyCharArray = new ListBuffer[Char]()

    if (size != 0) {
      var postSizeAcc: Long = 0;
      while (!break2) {


        val buf = Array.ofDim[Char](3000)
        val value = in.read(buf)
        if (value == -1) {
          return ""
        }

        logger.log(akka.event.Logging.LogLevel(3), "-----size body------" + buf.size)
        for (i <- 0 to value - 1) {
          bodyCharArray += buf(i)
          postSizeAcc += 1
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

  def readHeader(in: Reader): String = {

    var strinBuilder = new scala.collection.mutable.StringBuilder(1000)
    var break: Boolean = false;
    while (!break) {
      //val char = in.read
      val buf = Array.ofDim[Char](3000)

      val value = in.read(buf)

      if (value == -1) {
        return ""
      }


      breakable {
        for (i <- 0 to value - 1) {
          if (strinBuilder.size >= 3) {
            if (buf(i) == '\n' && strinBuilder(strinBuilder.size - 2) == '\n') {
              break = true
              break
            }
          }
          strinBuilder.append(buf(i))
        }
      }

    }
    val header =
      if (strinBuilder.size != 0)
      //  (for(i <- headerCharArray) yield(i))(collection.breakOut)
        strinBuilder.mkString
      else ""

    header
  }
}


object Main extends App {
  val system: ActorSystem = ActorSystem.create();
  //println("----------------------"+system.settings);
  private val log = Helpers.logger(getClass.toString)
  println("FROM SERVER: The Platypus is starting")
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
    serverSocket.setReuseAddress(true);
  } else {
    log.log(Level.SEVERE, "Port number " + Configuration.port + " is already being used--"+Configuration.generators)

  }
  var queueArray:ArrayBuffer[LinkedBlockingQueue[HttpRequest]] = new ArrayBuffer[LinkedBlockingQueue[HttpRequest]](4)

  for(i <- 0 to 3){
    queueArray.append(new LinkedBlockingQueue[HttpRequest]())
  }
  for (i <- 1 to Configuration.generators) {
    lib.actionRouters.connectionRouters.workers ! server.Fire(i, queueArray(i%1))
  }
  serverSocket.setPerformancePreferences(0, 2, 0)
  var i = 0;
  var cnt:Int = 0
  while (true) {
    val clientSocket = serverSocket.accept;
    clientSocket.setSoTimeout(Configuration.timeoutMilliseconds)
    clientSocket.setTcpNoDelay(true)
    clientSocket.isConnected
    log.log(Level.INFO, "-----------is it tcp_nodel-----creating connection-------------------" + i + "----" + clientSocket.getTcpNoDelay)


    val out = new BufferedOutputStream(clientSocket.getOutputStream, 1024)
    val stream = new InputStreamReader(clientSocket.getInputStream)
    val in = new BufferedReader((stream), 1000)
    val request = RequestConnectionFactory.generateRequestConnection(in, out, System.nanoTime(), stream, clientSocket)
    queueArray(cnt%1).offer(request)
    //workersQueue.offer(request)

  }

  lib.actionRouters.connectionRouters.system.shutdown()

}