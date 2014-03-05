package application.mvc

import server.lib.HttpRequest
import server.lib.Helpers._
import application.mvc._
import application.mvc
import net.liftweb.json._
/**
 * Created by hernansaab on 2/27/14.
 */
object ApplicationRouter {

  def runViewController(r: HttpRequest):Boolean = {
    println("parse path-----"+r.path)
    r.path match {
      case r"/index" =>  {
        r->>@ views.Default.index.html.display(controllers.DefaultController.index(r.command))

      }
      case r"/test" =>  r->>@ views.Json(controllers.DefaultController.test())

      case r"^/shopping/api/v1/json" => {
          r.command match {
            case "POST" =>    r->>@ views.Json(controllers.ShoppingController.update(r.command, r.body))
            case "GET" =>     r->>@ views.Json(controllers.ShoppingController.retrieve(r.command))
            case "DELETE" =>  r->>@ views.Json(controllers.ShoppingController.remove(r.command))
            case _ => r->>@ views.Json(controllers.ShoppingController(() => controllers.ShoppingController.unsupported(r.command)))

        }
      }
      case r"^.+/json$$" => r->>@ views.Json.genericUnsupported(r.path)

      case _ => r->>@ views.headers.Common.response415
    }


  }
}
