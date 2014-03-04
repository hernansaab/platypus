package server.lib

import scala.util.matching.Regex

/**
 * Created by hernansaab on 2/27/14.
 */
object Helpers {
  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }
}
