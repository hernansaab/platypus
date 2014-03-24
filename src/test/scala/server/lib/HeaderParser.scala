import java.io.{StringWriter, PrintWriter, ByteArrayOutputStream}
import org.scalatest._
import server.lib._
/**
 * Created by hernansaab on 3/1/14.
 */
class HeaderParser   extends FunSuite{

  test("Header parser 1"){

    var header =
      """GET /index HTTP/1.1
        |Content-type: application/json
        |Content-Length: 100
        |""".stripMargin


    assert(RequestConnectionFactory.Utils.parsePostSize(header) == 100)
    val strOut:StringWriter = new StringWriter();
    val request = RequestConnectionFactory.generateRequestConnection(header, null, strOut, () => {})
    assert(request.command == "GET")
    assert(request.path == "/index")
    assert(request.argument == null)
    assert(request.httpVersion == "HTTP/1.1")
  }

}