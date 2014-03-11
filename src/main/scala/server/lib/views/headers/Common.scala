package server.lib.views.headers

import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}
import java.util.TimeZone

/**
 * Created by hernansaab on 3/1/14.
 */
object Common {
  def json(size:Int):String = {
    val dt:DateTime = DateTime.now(DateTimeZone.forID("GMT"))
    val fmt:DateTimeFormatter = DateTimeFormat.forPattern("E, dd MMM yyyy kk:mm:ss");
    val date:String = fmt.print(dt)+ " GMT";
    f"""HTTP/1.1 200 Accepted
      |Content-type: application/json
      |Content-Length: ${size}
      |Date: ${date}
      |Server: Platypus
      |
      |""".stripMargin
  }

  def response415:String =
    """HTTP/1.1 415 Unsupported Media Type
      |
      |""".stripMargin

  def response200HtmlNoCookies(size:Int): String =
    response200Html(size)

  def response404NotFound():String = {
    val dt:DateTime = DateTime.now(DateTimeZone.forID("GMT"))
    val fmt:DateTimeFormatter = DateTimeFormat.forPattern("E, dd MMM yyyy kk:mm:ss");
    val date:String = fmt.print(dt)+ " GMT";

    f"""HTTP/1.0 404 Not Found
      |Date: ${date}
      |Server: Platypus
      |Content-Length: 14
      |Connection: close
      |Content-Type: text/html
      |
      |"""stripMargin
  }


  def response200Html(size:Long): String ={
    val dt:DateTime = DateTime.now(DateTimeZone.forID("GMT"))
    val fmt:DateTimeFormatter = DateTimeFormat.forPattern("E, dd MMM yyyy kk:mm:ss");
    val date:String = fmt.print(dt)+ " GMT";
    f"""HTTP/1.1 200 OK
Connection: Closed
Content-Length: ${size}
Date: ${date}
Server: Platypus

""".stripMargin
  }

}


