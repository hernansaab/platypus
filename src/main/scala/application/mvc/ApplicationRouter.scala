package application.mvc


import server.lib._
import server.lib.Helpers._
import application.mvc._
import application.mvc
import net.liftweb.json._
/**
 * Created by hernansaab on 2/27/14.
 */
object ApplicationRouter {
  def runViewController(r: HttpRequest):Boolean = {
    r.x.path match {
      case "" | "/" | r"/index"=> {
        val x = controllers.DefaultController.index(r.x.command)
        r@<<- x //controller renders view directly through a ssp file set in the controller/action DefaultController/index

      }
      case r"/index2"=> {
        r@<<- controllers.DefaultController.index2(r.x.command) //controller renders view directly through a ssp file set in the controller/action DefaultController/index

      }
      case r"/index3"=> {
        r@<<- controllers.DefaultController.index3(r.x.command) //controller renders view directly through a ssp file set in the controller/action DefaultController/index

      }

      case r"/test" =>  r@<<- views.Json(controllers.DefaultController.test())

      case r"^/shopping/api/v1/json" => {
        r.x.command match {
            case "POST" =>    r@<<- views.Json.<-- (controllers.ShoppingController.update(r.x.command, r.x.body))
            case "GET" =>     r@<<- views.Json.<-- (controllers.ShoppingController.retrieve(r.x.command))
            case "DELETE" =>  r@<<- views.Json.<-- (controllers.ShoppingController.remove(r.x.command))
            case _ => r@<<- views.Json.<-- (controllers.ShoppingController(() => controllers.ShoppingController.unsupported(r.x.command)))

        }
      }
      case r"^.*what/json$$" => r@<<- views.Json.<--(controllers.ShoppingController.response4("blah"))
      case r"^.*/json$$" => r@<<- views.Json.<--(controllers.ShoppingController.unsupported("blah"))


     // case r"^.+/json$$" => r@<<- views.Json.genericUnsupported(r.x.path)

      case _ => r@<<- views.headers.Common.response415
    }


  }
}
