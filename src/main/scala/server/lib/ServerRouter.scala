package server.lib


import java.io.PrintWriter
import org.slf4j.LoggerFactory
import application.mvc.ApplicationRouter
import com.typesafe.config.ConfigFactory
import server.{server, lib, Configuration}
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


    println("the path is-------" + request.x.path)
    if (command == Configuration.staticPath || request.x.path == "/favicon.ico") {
      _routeStatic(request);
    } else {
      ApplicationRouter.runViewController(request)
    }
    true
  }

  def route(request: HttpRequest): Boolean = {

    try {
      do {
        log.log(Level.INFO, "\n\nWAITING: connection router ------" + request.startTime + "======" + request.transactionCount.intValue() + "------------->" + request.currentTransactionIndex.intValue())

        while (!(request.transactionCount.intValue() > (request.currentTransactionIndex.intValue() + 1))) {
          Thread.sleep(0, 1000)
        }

        log.log(Level.INFO, "\n\nWAITING: connection routerxxxxx ------" + request.startTime + "======" + request.transactionCount.intValue() + "------------->" + request.currentTransactionIndex.intValue())

        log.log(Level.INFO, "WAITING OVER: connection router ------" + request.startTime)

        request.currentTransactionIndex.incrementAndGet()


        if(request.x.isClosedTransaction){
          println("breaking a closing transaction!!-------------------------")
          return true //break loop is it is closed
        }
        val durationr: Long = System.nanoTime() - request.x.startTime

        log.log(Level.INFO, "Delay is from router ------" + durationr / 1000000 + "-----" + request.startTime + "path to route is " + request.x.path)

        var error = _errorRoute(request)
        if (!request.x.isClosedTransaction && !error) {
          _route(request)
          request.out.flush()
        }else if(error){
          request.out.flush()
        }


        val duration: Long = System.nanoTime() - request.x.startTime
        log.log(Level.INFO, "Delay is ------" + duration / 1000000 + "---" + request.startTime + "----connectiontype---" + request.x.connectionType + "\n")
      } while (request.x.connectionType != "close" && request.x.isClosedTransaction != true)

    } catch {
      case e: Exception => {
        log.log(Level.WARNING, "ROUTE WARNING: Connection possibly closed by client\n" + e.getStackTraceString)
      }
    }
    log.log(Level.INFO, "----------OUT OF WAITING!! OVER: connection router -----CONNECTION OUT OF LOOP AND NO LONGER WAITING-" + request.startTime + "")

    request.cleanup()
    return true
  }

  def _errorRoute(request: HttpRequest): Boolean = {
    println("+++++++++++++++ error code for errorRoute-----" + request.x.contentType)
    println("+++++++++++++++ error header for errorRoute-----" + request.x.header)
    println("+++++++++++++++ error header for errorRoute-----" + request.x.path)

    if (!List(//only encoding types we are currently supporting
      "application/x-www-form-urlencoded",
      "application/jsonrequest",
      "application/json"
    ).contains(request.x.contentType) && request.x.contentType != "" && request.x.body != "") {
      println("+++++++++++++++ ERROR header for errorRoute-----")

      println("+++++++++++++++ ERROR header for errorRoute-----" + request.x.path)
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

