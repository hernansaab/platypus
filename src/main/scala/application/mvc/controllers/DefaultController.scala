package application.mvc.controllers

import org.fusesource.scalate._
import scala.text.Document
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import org.fusesource.scalate.{TemplateEngine, Binding, RenderContext}

import java.io.File
import server.lib.View

/**
 * Created by hernansaab on 2/27/14.
 */
object DefaultController {
  def index(get: String): String = {
    View.html("templates/index.ssp", Map("name" -> ("Hiram", "Chirino"), "city" -> "Tampa"))
  }

  def index2(get: String): String = {
    View.html("templates/index.ssp", Map("name" -> ("Judas", "Hernancito"), "city" -> "Buenos Aires"))
  }

  def index3(get: String): String = {
    View.html("templates/index.ssp", Map("name" -> ("German", "Ferreira"), "city" -> "Neuqyeb"))
  }


  def test(): Map[String, Any] = {

    Map("2323" -> "dfsdf")
  }

}
