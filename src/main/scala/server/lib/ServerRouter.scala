package server.lib


import java.io.PrintWriter
import org.slf4j.LoggerFactory
import application.mvc.ApplicationRouter
import com.typesafe.config.ConfigFactory
import server.Configuration
import java.util.logging.{Level, Logger}

/**
 * Created by hernansaab on 2/27/14.
 */

object ServerRouter {

  private val log = Logger.getLogger(getClass.toString)

  private def _route(request: HttpRequest): Boolean = {
    //GET /static

    val command =
      if (request.x.path.size > Configuration.staticPath.size)
        request.x.path.substring(0, Configuration.staticPath.size)
      else
        ""

    if (command == Configuration.staticPath) {
      _routeStatic(request);
    } else {
      ApplicationRouter.runViewController(request)
    }
    true
  }

  def route(request: HttpRequest): Boolean = {

    try {
      do {

        while (request.transactionCount.intValue() <= (request.currentTransactionIndex.intValue() + 1)) {
          Thread.sleep(0,1000)
        }


        request.currentTransactionIndex.incrementAndGet()
        val durationr:Long = System.nanoTime() - request.x.startTime

        println("Delay is from router ------"+ durationr/1000000)

        if (!request.x.isClosedTransaction && !_errorRoute(request)) {
          _route(request)
        }


        val duration:Long = System.nanoTime() - request.x.startTime
        println("Delay is ------"+ duration/1000000)
      } while (request.x.connectionType != "close" || request.x.isClosedTransaction == true)

    } catch {
      case e: Exception => {
        log.log(Level.WARNING, "ROUTE WARNING: Connection possibly closed by client\n" + e.getStackTraceString)
      }
    }

    return true
  }

  def _errorRoute(request: HttpRequest): Boolean = {

    if (!List(//only encoding types we are currently supporting
      "application/x-www-form-urlencoded",
      "application/jsonrequest",
      "application/json"
    ).contains(request.x.contentType) && request.x.body != "") {
      views.headers.Common.response415
      return true
    }
    false
  }


  def _routeStatic(request: HttpRequest): Boolean = {
    val path =
      if (request.x.path == "/static")
        StaticResources.loadResourcess(request, "index.html")
      else
        StaticResources.loadResourcess(request, request.x.path.substring(Configuration.staticPath.size + 1))
    true
  }
}

