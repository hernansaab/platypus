package server.lib.views
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.codahale.jerkson.Json._


import scala.text.Document
import server.lib.{Helpers, views}
import application.mvc.controllers
import java.util.logging.Level

/**
 * Created by hernansaab on 2/28/14.
 */
object Json {

  private val log = Helpers.logger(getClass.toString)

  def apply(data:Map[String, Any]):String ={
    val content:String =  generate(data)
    val ret = headers.Common.json(content.size)+content
    ret

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
