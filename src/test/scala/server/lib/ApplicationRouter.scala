import java.io.StringWriter
import java.util.logging.{Level, Logger}
import org.scalatest._
import server.Configuration
import server.lib._
import application.mvc._
import server.lib.Helpers._

/**
 * Created by hernansaab on 3/1/14.
 */
class ApplicationRouter extends FunSuite{
  private val log = Logger.getLogger(getClass.toString)

  test("router test 1"){
    var header =
      """GET /test HTTP/1.1
        |Content-type: application/json
        |Content-Length: 100
        |""".stripMargin
    var strOut:StringWriter = new StringWriter();
    var request = RequestConnectionFactory.generateRequestConnection(header, null, strOut,  () => {})
    ServerRouter.route(request);
    assert(strOut.toString  == """HTTP/1.1 200 Accepted
                                 |Content-type: application/json
                                 |Content-Length: 18
                                 |
                                 |[2,45,34,23,7,5,3]""".stripMargin)
    strOut = new StringWriter();
    header =
      """GET /test/json HTTP/1.1
        |Content-type: application/json
        |Content-Length: 100
        |""".stripMargin
    request = RequestConnectionFactory.generateRequestConnection(header, null, strOut,  () => {})
    ServerRouter.route(request);
    assert(strOut.toString == """HTTP/1.1 200 Accepted
                                |Content-type: application/json
                                |Content-Length: 106
                                |
                                |{
                                |  "command":{
                                |    "status":"ERROR",
                                |    "path":"/test/json",
                                |    "errors":["Unsupported command."]
                                |  }
                                |}""".stripMargin)





  }


  test("router test 2"){
    Booter.start()

    var header =
      """GET /index HTTP/1.1
        |""".stripMargin
    var strOut = new StringWriter();
    var request = RequestConnectionFactory.generateRequestConnection(header, null, strOut,  () => {})
    ServerRouter.route(request);
    assert(strOut.toString.matches("""(?s)HTTP/1.1 200 OK\nConnection: Closed\nContent-Length:\s71\nDate:\s+.+"""))

  }

  test("router static 1"){
    Booter.start()

    var header =
      """GET /static/index HTTP/1.1
        |""".stripMargin
    var strOut = new StringWriter();
    var request = RequestConnectionFactory.generateRequestConnection(header, null, strOut,  () => {})
    ServerRouter.route(request);
    log.log(Level.WARNING, "debug strOut--"+strOut)
    assert(strOut.toString.matches("""(?s)HTTP/1.1 200 OK\nConnection: Closed\nContent-Length:\s71\nDate:\s+.+"""))

  }
}

