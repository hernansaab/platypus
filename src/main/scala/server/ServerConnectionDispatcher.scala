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
import java.util.concurrent.{ExecutorService, Executors, LinkedBlockingQueue}
import scala.util.control.Breaks._
import com.lmax.disruptor.EventFactory
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.RingBuffer
import com.lmax.disruptor.dsl.Disruptor

;

/**
 * Created by hernansaab on 2/26/14.
 */


class ServerConnectionDispatcher extends EventHandler[ValueEvent] {


  private val log = Logger.getLogger(getClass.toString)

  def delayNanoseconds(delay: Int) = {
    val t = System.nanoTime()
    while ((System.nanoTime() - t) < delay) {
      ////
    }
  }

  def onEvent(event: ValueEvent, sequence: Long, endOfBatch: Boolean):Unit = {

    val request = event.request
    if (request == null) break()

    if ((System.nanoTime() - request.lastRead) / 1000000 > Configuration.timeoutMilliseconds) {
      request.cleanup()
      return
    }
    var success = true

    var ready = false
    try {
      ready = request.in.ready()
    } catch {
      case e: Throwable =>
        log.log(Level.WARNING, ("Connection  closed by cli timeout??"))
        request.cleanup()
        return
    }

    if (ready) {
      try {
        success = readRequest(request)
      }
      catch {
        case e: Throwable => log.log(Level.WARNING, ("Connection possibly closed by client---" + e.getMessage).+("\n---"))
          request.addTransaction(new SingleTransaction(null))
          request.cleanup()
          success = false
      }
    } else {
      val seq:Long = actionRouters.connectionRouters.ringBuffer.next()
      val valueEvent: ValueEvent =  actionRouters.connectionRouters.ringBuffer.get(seq)
      valueEvent.request = request
      actionRouters.connectionRouters.ringBuffer.publish(seq)
      return
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
      return
    }
    val seq:Long = actionRouters.connectionRouters.ringBuffer.next()
    val valueEvent: ValueEvent =  actionRouters.connectionRouters.ringBuffer.get(seq)
    valueEvent.request = request;
    actionRouters.connectionRouters.ringBuffer.publish(seq)
    return

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
        log.log(Level.WARNING, ("Cssssssonnection  closed by cli timeout??"))

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

        log.log(Level.WARNING, "-----size body------" + buf.size)
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

class ValueEvent {
  var request:HttpRequest = null
  def getValue():HttpRequest = {
    return request
  }
  def setValue(_request:HttpRequest) = {
    request = _request
  }
}
object ValueEvent {
  def EVENT_FACTORY:EventFactory[ValueEvent]  = new EventFactory[ValueEvent]() {
    def  newInstance():ValueEvent = {
      return new ValueEvent();
    }
  };
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
    log.log(Level.SEVERE, "Port number " + Configuration.port + " is already being used--" + Configuration.generators)

  }

  val exec:ExecutorService = Executors.newCachedThreadPool();
  // Preallocate RingBuffer with 1024 ValueEvents
  val disruptor: Disruptor[ValueEvent] = new Disruptor[ValueEvent](ValueEvent.EVENT_FACTORY, 1024, exec);


  val handler = new ServerConnectionDispatcher()
  disruptor.handleEventsWith(handler);
  actionRouters.connectionRouters.ringBuffer  = disruptor.start();

  serverSocket.setPerformancePreferences(0, 2, 0)
  var i = 0;
  var cnt: Int = 0
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


    val seq:Long = actionRouters.connectionRouters.ringBuffer.next();
    val valueEvent:ValueEvent = actionRouters.connectionRouters.ringBuffer.get(seq)
    valueEvent.request = request;
    actionRouters.connectionRouters.ringBuffer.publish(seq);


    //workersQueue.offer(request)

  }

  lib.actionRouters.connectionRouters.system.shutdown()

}