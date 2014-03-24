package server.lib

import scala.util.matching.Regex
import java.io._
import java.net.Socket

/**
 * Created by hernansaab on 2/27/14.
 */
object RequestConnectionFactory {


  def generateRequestConnection(in:Reader, out:BufferedOutputStream, ts:Long, stream:InputStreamReader, socket:Socket): HttpRequest = {
    val request = new HttpRequest(in, out, ts, stream)
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
