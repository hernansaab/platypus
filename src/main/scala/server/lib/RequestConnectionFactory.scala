package server.lib

import scala.util.matching.Regex
import java.io.{Writer, PrintWriter, BufferedReader}

/**
 * Created by hernansaab on 2/27/14.
 */
object RequestConnectionFactory {


  def generateRequestConnection(in:BufferedReader, out:Writer, cleanup:() => Unit): HttpRequest = {
    val request = new HttpRequest(in, out, cleanup)
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
    request
  }



}
