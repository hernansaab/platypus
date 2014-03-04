package server.lib

import scala.util.matching.Regex
import java.io.{PrintWriter, BufferedReader}

/**
 * Created by hernansaab on 2/27/14.
 */
class HttpRequest(_in:BufferedReader, _out:PrintWriter,_cleanup:()=>Unit) {
  var body:String = "";
  var header:String = ""
  var path:String = ""
  var command:String = ""
  var argument:String = ""
  var httpVersion: String = ""
  var contentType: String = ""
  var contentEncoding: String = ""
  var postSize: Int=0
  private val in:BufferedReader= _in
  private val out:PrintWriter = _out
  private val cleanup:() => Unit = _cleanup

  /**
   * print a string to output connection and close it
   * @param text
   * @return status successfull
   */
  def ->>@(text:String):Boolean = {
    out.print(text);
    cleanup()
    true
  }
  /**
   * print a string to output connection but keep connection open
   * @param text
   * @return status successfull
   */
  def ->>(text:String):Boolean = {
    out.print(text)
    out.flush()
    true
  }
}
