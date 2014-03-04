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

  def response200HtmlNoCookies: String =
    """
      |Connection: close
      |Vary:	Accept-Encoding
      |Content-Type:	text/html; charset=iso-8859-1
      |Server:	Apache
      |""".stripMargin
}
