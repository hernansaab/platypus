package server.lib

import scala.util.matching.Regex
import java.util.logging.{Handler, FileHandler, Level, Logger}


/**
 * Created by hernansaab on 2/27/14.
 */
object Helpers {

  //val handler:Handler = new FileHandler("test.log", Configuration.log_size, LOG_ROTATION_COUNT);

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

 // def logger(arg:String):Logger = {

  //}
}
