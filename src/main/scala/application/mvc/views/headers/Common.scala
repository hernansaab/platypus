package application.mvc.views.headers

/**
 * Created by hernansaab on 3/1/14.
 */
object Common {
  def json(size:Int):String =
    f"""HTTP/1.1 200 Accepted
      |Content-type: application/json
      |Content-Length: ${size}
      |
      |""".stripMargin

  def response415:String =
    """HTTP/1.1 415 Unsupported Media Type
      |
      |""".stripMargin

  def response200HtmlNoCookies(size:Int): String =

    """HTTP/1.1·200·OK
      |Accept-Ranges: bytes
Connection: Keep-Alive
Content-Length:${size}
Content-Type: text/html
Date: Wed, 05 Mar 2014 10:12:41 GMT
Server:Platypus
Vary:Accept-Encoding

    """.stripMargin
}
