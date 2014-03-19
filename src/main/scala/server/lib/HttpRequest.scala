package server.lib

import scala.util.matching.Regex
import java.io._
import java.util.concurrent.atomic.AtomicInteger
import org.joda.time.{LocalDate, DateTime}
import java.security.SecureRandom
import java.util.logging.{Level, Logger}
import java.util.concurrent.{LinkedBlockingQueue, SynchronousQueue, ConcurrentLinkedDeque}

/**
 * Created by hernansaab on 2/27/14.
 */
class HttpRequest(_in:Reader, _out:BufferedOutputStream, ts:Long, _inputStream: InputStreamReader) {

 private val log = Logger.getLogger(getClass.toString)
 private var current:SingleTransaction = null
  var firstCharReady = false
  val in= _in
  val out:BufferedOutputStream = _out
  var startTime = ts
  val inputStream: InputStreamReader = _inputStream
  @volatile var currentTransactionIndex:AtomicInteger = new AtomicInteger(-1)
  @volatile var transactionCount:AtomicInteger = new AtomicInteger(0)
  @volatile var transactionsQ:LinkedBlockingQueue[SingleTransaction] = new LinkedBlockingQueue[SingleTransaction]()
  /**
   * print a string to output connection and close it
   * @param text
   * @return status successfull
   */
  def copy():HttpRequest = {
    val r = new HttpRequest(in, out, ts, inputStream)
    r.transactionsQ = transactionsQ
    r.currentTransactionIndex = currentTransactionIndex
    r.transactionCount = transactionCount
    r.startTime = startTime
    r
  }
  def @<<-(text:String):Boolean = {
    try{

      out.write(text.getBytes)
      out.flush()

      val ts3 = System.nanoTime()

    }catch{
      case e:Throwable => {
        log.log(Level.INFO, "Unable to write response because connection already closed:\n"+e.getStackTraceString)
      }
    }
    true
  }
  def x():SingleTransaction = {
    current
  }
  /**
   * print a string to output connection but keep connection open
   * @param text
   * @return status successfull
   */
  def <<-(text:String):Boolean = {
    out.write(text.toByte)
    out.flush()
    true
  }


  def blockingReadTransaction():SingleTransaction = {
    current = transactionsQ.take()
    return current
  }

  def ^<--():Boolean = {
    cleanup()
    true
  }

  def append(c:Int):Boolean = {

    out.write(c)
    out.flush()
    true
  }


  def cleanup(): Unit = {
    try {
      out.flush()
    } catch {
      case e: Throwable => log.log(Level.INFO, "Connection possibly timed out before we close it--1-" + e.getMessage + ("\n---") + e.getStackTrace)
    }
    try {
      in.close()
    } catch {
      case e: Throwable => log.log(Level.INFO, ("Connection possibly timed out before we close it--3-" + e.getMessage).+("\n---") + e.getStackTrace)
    }
    try {
      out.close()
    } catch {
      case e: Throwable => log.log(Level.INFO, ("Connection possibly timed out before we close it--4-" + e.getMessage).+("\n---") + e.getStackTrace)
    }
    try {
      inputStream.close()
    } catch {
      case e: Throwable => log.log(Level.INFO, ("Connection possibly timed out before we close it--2-" + e.getMessage + ("\n---") + e.getStackTrace))
    }
  }


  def addTransaction(transaction:SingleTransaction): Boolean = {
    transactionsQ.add(transaction)
    transactionCount incrementAndGet()
    true;
    /* val(command, path, argument, httpVersion) = Utils.parseGetCommand(header)
 request.command = command
 request.path = path
 request.argument = argument
 request.httpVersion = httpVersion
 request.contentType = Utils.parseContentType(header)
 request.contentEncoding = Utils.parseContentEncoding(header)
 request.postSize = Utils.parsePostSize(header)
 request.header = header
 request.connectionType = Utils.parseConnectionType(header)
*/
  }
}
