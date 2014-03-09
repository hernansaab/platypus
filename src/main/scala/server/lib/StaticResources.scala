package server.lib

import java.io.{IOException, DataInputStream, File}
import scala.io.Source
import scala.collection.mutable
import org.apache.commons.codec.binary._
import org.apache.commons.io.IOUtils
import java.util.logging.{Level, Logger}

/**
 * Created by hernansaab on 3/6/14.
 */
object StaticResources {
  private val log = Logger.getLogger(getClass.toString)

  def loadResourcess(request: HttpRequest, pathToResource: String): Boolean = {


    try {
      val file = new java.io.File("src/main/resources/external/" + pathToResource)
      if (file.length() != 0 && !file.isDirectory) {
        request @<<- (views.headers.Common.response200Html(file.length) + scala.io.Source.fromFile("src/main/resources/external/" + pathToResource).mkString)
      } else {

        request @<<- (views.headers.Common.response404NotFound())
      }
    } catch {
      case e: Exception =>
      {
        log.log(Level.WARNING, "Unable to read static file reasource:" + pathToResource)
        request @<<- (views.headers.Common.response404NotFound())
      }
    }

    true

  }
}
