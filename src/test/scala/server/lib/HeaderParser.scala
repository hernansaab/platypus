import org.scalatest._
import server.lib._
/**
 * Created by hernansaab on 3/1/14.
 */
class HeaderParser   extends FunSuite{

  test("Header parser 1"){
    println("------ssshey------")

    var header =
      """GET /index HTTP/1.1
        |Content-type: application/json
        |Content-Length: 100
        |""".stripMargin

    assert(RequestConnectionFactory.parsePostSize(header) == 100)
    val (command, path, argument, httpVersion) = RequestConnectionFactory.parseGetCommand(header)
    assert(command == "GET")
    assert(path == "/index")
    assert(argument == null)
    assert(httpVersion == "HTTP/1.1")
  }

}