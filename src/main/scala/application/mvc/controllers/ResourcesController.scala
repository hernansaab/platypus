package application.mvc.controllers

import scala.collection.mutable.ArrayBuffer

/**
 * Created by hernansaab on 4/3/14.
 */
object ResourcesController extends AppController{

  def delete(arg:String): Map[String, Any] ={
    null
  }

  def update(arg:String): Map[String, Any] = {
    null
  }

  def handler(command:String, endPoint:String, split:ArrayBuffer[String]): Map[String,Any] = {
    command match {
      case "GET" => {
        return Map(
          "message" ->endPoint
        )
      }
      case _ => {
        return Map(
          "message" ->command
        )
      }
    }
    return Map(
      "message" -> (command+":unknown")
    )

  }

}
