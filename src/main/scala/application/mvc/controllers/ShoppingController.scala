package application.mvc.controllers
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
/**
 * Created by hernansaab on 2/27/14.
 */
object ShoppingController extends AppController{
  def update(command:String, post:String): Map[String, Any] ={
    null
  }

  def remove(arg:String): Map[String, Any] ={
    null
  }

  def retrieve(arg:String): Map[String, Any] ={
    null
  }

  def unsupported(arg:String): Map[String, Any] ={

    (
      Map(arg ->  ("status" -> "ERROR") ~ ("errors" -> List("Unsupported command.")))
    )
  }

  def response4(arg:String): Map[String, Any] ={

    Map(
      "message" ->"Hello, World!"
      )
  }
}
