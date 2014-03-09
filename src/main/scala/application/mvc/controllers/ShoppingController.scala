package application.mvc.controllers
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
/**
 * Created by hernansaab on 2/27/14.
 */
object ShoppingController extends AppController{
  def update(command:String, post:String): JValue ={
    null
  }

  def remove(arg:String): JValue ={
    null
  }

  def retrieve(arg:String): JValue ={
    null
  }

  def unsupported(arg:String): JValue ={

    (
      arg ->  ("status" -> "ERROR") ~ ("errors" -> List("Unsupported command."))
    )
  }

  def response4(arg:String): JValue ={

    (
      arg ->  ("status" -> "DOUBT") ~ ("errors" -> List("I dont know if i should support this command command."))
      )
  }
}
