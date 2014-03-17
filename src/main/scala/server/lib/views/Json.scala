package server.lib.views
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.codahale.jerkson.Json._


import scala.text.Document
/**
 * Created by hernansaab on 2/28/14.
 */
object Json {
  def apply(data:Map[String, Any]):String ={

    val content:String =  generate(data)

   // val content:String = generate(Map("one"->1, "two"->"dos"))
    headers.Common.json(content.size)+content
  }

  def genericUnsupported(path:String):String = {
    val content = compact(render((
      "command" ->  ("status" -> "ERROR") ~ ("path" -> path) ~ ("errors" -> List("Unsupported command."))
      )))
    headers.Common.json(content.size)+content
  }

  def <--(data:Map[String, Any]):String ={
    apply(data)
  }


}
