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

    println("---------------------trying to find file----"+pathToResource)
    try {
      val file = new java.io.File("src/main/resources/external/" + pathToResource)
      if (file.length() != 0 && !file.isDirectory) {
        val bodyp =  scala.io.Source.fromFile("src/main/resources/external/" + pathToResource)("ISO-8859-1")
        println("---------------debug -----"+bodyp.size)
  //      val body = Source.getClass().getResource("/lesson4/test.txt")
        request @<<- (views.headers.Common.response200Html(file.length) + bodyp.mkString)
      } else {
        log.log(Level.WARNING, "Unable 2to read static file reasource:" + pathToResource+":")

        request @<<- (views.headers.Common.response404NotFound())
      }
    } catch {
      case e: Throwable =>
      {
        log.log(Level.WARNING, "Unable to read static file reasource:" + pathToResource+":"+e.getMessage+":"+ e.getClass.toString+":\n"+e.getStackTraceString)
        request @<<- (views.headers.Common.response404NotFound())
      }
    }

    true

  }
}
