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

  private val log = Logger.getLogger(getClass.toString)

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

  def route(request: HttpRequest, reroute:()=>Unit): Boolean = {

    try {
      do {

        var stamp = System.currentTimeMillis()
        while (!(request.transactionCount.intValue() > (request.currentTransactionIndex.intValue() + 1))) {
          if(System.currentTimeMillis() - stamp > 300){
            reroute()
            return true
          }
          Thread.sleep(1)
        }


        request.currentTransactionIndex.incrementAndGet()


        if(request.x.isClosedTransaction){
          return true //break loop is it is closed
        }
        val durationr: Long = System.nanoTime() - request.x.startTime


        val error = _errorRoute(request)
        if (!request.x.isClosedTransaction && !error) {
          _route(request)
          request.out.flush()
        }else if(error){
          request.out.flush()
        }


        val duration: Long = System.nanoTime() - request.x.startTime
      } while (request.x.connectionType != "close" && request.x.isClosedTransaction != true)

    } catch {
      case e: Exception => {
        log.log(Level.WARNING, "ROUTE WARNING: Connection possibly closed by client\n" + e.getStackTraceString+":"+e.getMessage)
      }
    }

    request.cleanup()
    return true
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

