package application.mvc.views
import net.liftweb.json._
import net.liftweb.json.JsonDSL._


import scala.text.Document
/**
 * Created by hernansaab on 2/28/14.
 */
object Json {
  def apply(data:JValue):String ={

    val content:String = pretty(render(data))
    headers.Common.json(content.size)+content
  }

  def genericUnsupported(path:String):String = {
    val content = pretty(render((
      "command" ->  ("status" -> "ERROR") ~ ("path" -> path) ~ ("errors" -> List("Unsupported command."))
      )))
    headers.Common.json(content.size)+content
  }

}
