package server.lib

import scala.collection.mutable.ArrayBuffer
import java.util.logging.{Level, Logger}

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
      Utils.parseGetCommand(header)
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
  object Utils {

    def parsePostSize(_header: String): Int = {
      val pattern = "Content-Length: (\\d+)".r
      var list = pattern.findAllIn(_header).matchData.map(m => m.group(1)).toList
      val size =
        if (list.size == 0) 0
        else list.head.toInt
      size
    }
    def parseGetCommand(header: String): (String, String, String, String, String, String, String, Int) = {

      /**
       * GET /tutorials/other/top-20-mysql-best-practices/ HTTP/1.1
Host: net.tutsplus.com
User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)
Accept: text/html,application/xhtml+xml,application/xml;q=0.9;q=0.8
      Accept-Language: en-us,en;q=0.5
      Accept-Encoding: gzip,deflate
      Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
      Keep-Alive: 300
      Connection: keep-alive
      Cookie: PHPSESSID=r2t5uvjq435r4q7ib3vtdjq120
      Pragma: no-cache
      Cache-Control: no-cache
       */


      val splitLines = header.split("\n").map(
        _.split(" ")
      )
      val headerArray:ArrayBuffer[String] = new ArrayBuffer[String]
      for( line <- splitLines){
        headerArray++=line
      }

      val command = headerArray(0)
      val httpVersion = headerArray(2)

      val sep = headerArray(1).lastIndexOf("/")
      val path = headerArray(1)
      val argument = headerArray(1).substring(sep+1)

      val contentType = {
        val index = headerArray.indexOf("Content-Type:")
        if(index == -1)
          ""
        else headerArray(index+1)
      }

      val contentEncoding = {
        val index = headerArray.indexOf("Content-Encoding:")
        if(index == -1)
          ""
        else headerArray(index+1)
      }

      val connectionType = {
        val index = headerArray.indexOf("Connection:")
        if(index == -1)
          "keep-alive"
        else headerArray(index+1).toLowerCase
      }



      val postSize = {
        val index = headerArray.indexOf("Content-Length:")
        if(index == -1)
          0
        else headerArray(index+1).toInt
      }


      try {
        (command, path, argument, httpVersion, contentType, contentEncoding, connectionType, postSize)
      } catch {
        case e: Exception => {
          ("", "", "", "", "", "", "", 0)
        }
      }
    }
    def parseContentType(header: String): String = {
      val pattern = "Content-Type: (\\d+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList
      var contentType =
        if (list.size == 0) ""
        else list.head

      if(contentType == null)
         contentType = ""
      contentType
    }
    def parseContentEncoding(header: String): String = {
      val pattern = "Content-Encoding:\\s(.+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList
      val value =
        if (list.size == 0) ""
        else list.head
      value
    }
    def parseConnectionType(header: String): String = {
      val pattern = "Connection:\\s(.+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList

      if (list.size == 0) "keep-alive"
      else list.head.toLowerCase()

    }

  }
}
