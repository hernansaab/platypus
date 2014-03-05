package server.lib


import java.io.PrintWriter
import org.slf4j.LoggerFactory
import application.mvc.ApplicationRouter

/**
 * Created by hernansaab on 2/27/14.
 */

object MvcRouter{

  private val log = LoggerFactory.getLogger(getClass)

  private def _route(request:HttpRequest):Boolean = {
    ApplicationRouter.runViewController(request)
    true
  }

  def route(request:HttpRequest):Boolean = {


    try{
      if(!_errorRoute(request)){
        println("-----good-----no server error in request------")
        _route(request)
      }
    }catch {
      case e: Exception =>{
        log.debug(e.getMessage+"\n"+e.getStackTrace)
      }
    }

    return true
  }

  def _errorRoute(request:HttpRequest):Boolean = {
    if(!List(//only encoding types we are currently supporting
      "application/x-www-form-urlencoded",
      "application/jsonrequest",
      "application/json"
    ).contains(request.contentType) && request.body != ""){
      application.mvc.views.headers.Common.response415
      return true
    }
    false
  }
}

