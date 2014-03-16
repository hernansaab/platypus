package server.lib


import java.io.PrintWriter
import org.slf4j.LoggerFactory
import application.mvc.ApplicationRouter
import com.typesafe.config.ConfigFactory
import server.{server, lib, Configuration}
import java.util.logging.{Level, Logger}
import akka.actor.ActorRef

/**
 * Created by hernansaab on 2/27/14.
 */

object ServerRouter {

  private val log = Helpers.logger(getClass.toString)

  private def _route(request: HttpRequest): Boolean = {
    //GET /static

    val command =
      if (request.x.path.size > Configuration.staticPath.size)
        request.x.path.substring(0, Configuration.staticPath.size)
      else
        ""
    if (command == Configuration.staticPath || request.x.path == "/favicon.ico") {
      _routeStatic(request);
    } else {
      ApplicationRouter.runViewController(request)

    }
    true
  }


  /**
   *
   * @param request
   * @return Int if 1 transaction count is 0 and its open connection
   *         if 0 connection closed
   *         if 2 connection open and transaction occurred
   */
  def route(request: HttpRequest): Int = {
var action = ""
    try {


      if (!(request.transactionCount.intValue() > (request.currentTransactionIndex.intValue() + 1))) {
        return 1
      }
      request.currentTransactionIndex.incrementAndGet()


      if (request.x.isClosedTransaction) {
        log.log(Level.INFO, "Exit cause its broken")
        return 0 //break loop is it is closed
      }


      val error = _errorRoute(request)

      if (!request.x.isClosedTransaction && !error) {
        _route(request)
        val st2= System.nanoTime()
        action = "routed-----"
        request.out.flush()
      } else if (error) {

        request.out.flush()
      }


      val duration: Long = System.nanoTime() - request.x.startTime

      if(request.x.connectionType == "close" || request.x.isClosedTransaction == true){
        log.log(Level.INFO, "Exit cause its not done yet")
        return 0
      }

    } catch {
      case e: Throwable => {
        log.log(Level.WARNING, "ROUTE WARNING: Connection possibly closed by client\n" + e.getStackTraceString+":"+e.getMessage)
        return 0
      }
    }

    log.log(Level.INFO, "Exit cause its end of loop---"+action)

   // request.cleanup()
    return 2
  }

  def _errorRoute(request: HttpRequest): Boolean = {

    if (!List(//only encoding types we are currently supporting
      "application/x-www-form-urlencoded",
      "application/jsonrequest",
      "application/json"
    ).contains(request.x.contentType) && request.x.contentType != "" && request.x.body != "") {

      views.headers.Common.response415
      return true
    }
    false
  }


  def _routeStatic(request: HttpRequest): Boolean = {
    val path =
      if (request.x.path == "/static")
        StaticResources.loadResourcess(request, "index.html")
      else if (request.x.path == "/favicon.ico") {
        StaticResources.loadResourcess(request, "favicon.ico")
      }
      else
        StaticResources.loadResourcess(request, request.x.path.substring(Configuration.staticPath.size + 1))
    true
  }
}

