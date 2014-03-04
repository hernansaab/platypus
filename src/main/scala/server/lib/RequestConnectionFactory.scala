package server.lib

import scala.util.matching.Regex
import java.io.{PrintWriter, BufferedReader}

/**
 * Created by hernansaab on 2/27/14.
 */
object RequestConnectionFactory {

  object Utils {

    def parsePostSize(_header: String): Int = {
      val pattern = "Content-Length: (\\d+)".r
      var list = pattern.findAllIn(_header).matchData.map(m => m.group(1)).toList
      val size =
        if (list.size == 0) 0
        else list.head.toInt
      size
    }

    def parseGetCommand(header: String): (String, String, String, String) = {
      val FullCommand = "(?s)(GET|POST|PUT|DELETE|HEAD)\\s+([^\\?\\s]+)((\\?)?(\\S+)?)\\s+(\\S+).+".r
      try {
        val FullCommand(command, path, dummy1, dummy2, argument, httpVersion) = header
        (command, path, argument, httpVersion)
      } catch {
        case e: Exception => {
          (null, null, null, null)
        }
      }
    }

    def parseContentType(header: String): String = {
      val pattern = "Content-Type: (\\d+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList
      val contentType =
        if (list.size == 0) null
        else list.head
      contentType
    }

    def parseContentEncoding(header: String): String = {
      val pattern = "Content-Encoding:\\s(.+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList
      val value =
        if (list.size == 0) null
        else list.head
      value
    }

  }
  def generateRequestConnection(header: String, in:BufferedReader, out:PrintWriter, cleanup:() => Unit): HttpRequest = {
    val request = new HttpRequest(in, out, cleanup)
    val(command, path, argument, httpVersion) = Utils.parseGetCommand(header)
    request.command = command
    request.path = path
    request.argument = argument
    request.httpVersion = httpVersion
    request.contentType = Utils.parseContentType(header)
    request.contentEncoding = Utils.parseContentEncoding(header)
    request.postSize = Utils.parsePostSize(header)
    request.header = header

    request
  }


}
