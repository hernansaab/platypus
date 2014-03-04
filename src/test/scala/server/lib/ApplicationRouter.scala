import org.scalatest._
import server.lib._
import application.mvc._

/**
 * Created by hernansaab on 3/1/14.
 */
class ApplicationRouter extends FunSuite{
  test("router test 1"){
    var header =
      """GET /test HTTP/1.1
        |Content-type: application/json
        |Content-Length: 100
        |""".stripMargin
    var request = RequestConnectionFactory.generateRequestConnection(header, null, null, null)
    assert(ApplicationRouter.runViewController(request) == """HTTP/1.1 200 Accepted
                                                             |Content-type: application/json
                                                             |Content-Length: 18
                                                             |
                                                             |[2,45,34,23,7,5,3]""".stripMargin)

    header =
      """GET /test/json HTTP/1.1
        |Content-type: application/json
        |Content-Length: 100
        |""".stripMargin
    request = RequestConnectionFactory.generateRequestConnection(header, null, null, null)
    assert(ApplicationRouter.runViewController(request) == """HTTP/1.1 200 Accepted
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
}

