package application.mvc.controllers
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JValue

/**
 * Created by hernansaab on 3/1/14.
 */
class AppController {
  def apply(action:()=>JValue):JValue ={
    var results:JValue = List()
    if(beforeFilter()){
      results = action()
    }
    afterFilter()
    results
  }

  def beforeFilter():Boolean =  {
    return true;
  }

  def afterFilter():Boolean = {
    return true
  }


}
