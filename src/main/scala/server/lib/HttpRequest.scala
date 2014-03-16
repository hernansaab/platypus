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
class HttpRequest(_in:PushbackReader, _out:Writer, ts:Long, _inputStream: InputStreamReader,_cleanup:()=>Unit) {

 private val log = Logger.getLogger(getClass.toString)
 private var current:SingleTransaction = null
  var firstCharReady = false
  val in= _in
  val out:Writer = _out
  val cleanup:() => Unit = _cleanup
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
    val r = new HttpRequest(in, out, ts, inputStream, cleanup)
    r.transactionsQ = transactionsQ
    r.currentTransactionIndex = currentTransactionIndex
    r.transactionCount = transactionCount
    r.startTime = startTime
    r
  }
  def @<<-(text:String):Boolean = {
    try{
      out.write(text);
      out.flush()
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
    out.write(text)
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

    out.append(c.toChar)
    out.flush()
    true
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
