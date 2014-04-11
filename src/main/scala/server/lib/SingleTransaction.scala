package server.lib

import scala.collection.mutable.ArrayBuffer
import java.util.logging.{Level, Logger}
import java.util.StringTokenizer
import scala.collection.immutable.List
import scala.List
import scala.collection.mutable
import java.util
import java.nio.CharBuffer

/**
 * Created by hernansaab on 3/8/14.
 */
class SingleTransaction(_header:String) {



  val header = if(_header == null) "" else _header
  private val log = Logger.getLogger(getClass.toString)

  if(_header == null){

  }
  val(command, path, argument, httpVersion, contentType, contentEncoding, connectionType, postSize) =
    if(_header != null)
      HeaderUtils.parseGetCommand(header)
    else
      ("", "","", "", "" , "" ,"",  0)

  var body:String = null
  var startTime = System.nanoTime()
  val isClosedTransaction:Boolean =
    if(_header == null || _header == "") true
    else false

  def setBody(_body:String){
    body = _body
  }

}
