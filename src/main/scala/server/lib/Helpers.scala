package server.lib

import scala.util.matching.Regex
import java.util.logging._
import java.text.{SimpleDateFormat, DateFormat}
import java.util.Date
import akka.event.{LoggingAdapter, Logging}
import akka.actor.ActorSystem


/**
 * Created by hernansaab on 2/27/14.
 */
object Helpers {

  class BriefLogFormatter extends Formatter {

    val  format:DateFormat = new SimpleDateFormat("h:mm:ss")
    val  lineSep:String = System.getProperty("line.separator");
    /**
     * A Custom format implementation that is designed for brevity.
     */
    def  format( record:LogRecord):String  = {
       var loggerName:String = record.getLoggerName();
      if(loggerName == null) {
        loggerName = "root";
      }
       val output:StringBuilder = new StringBuilder()
        .append(loggerName)
   //     .append("[")
    //    .append(record.getLevel()).append('|')
     //   .append(Thread.currentThread().getName()).append('|')
    //    .append(format.format(new Date(record.getMillis())))
    //    .append("]: ")
         .append(format.format(new Date(record.getMillis())))
        .append(record.getMessage()).append(' ').append(lineSep);
      return output.toString();
    }


  }

  //val handler:Handler = new FileHandler("test.log", Configuration.log_size, LOG_ROTATION_COUNT);
  val handler:Handler = new FileHandler("log.log", 5000000, 10);
  handler.setFormatter(new BriefLogFormatter)

  //handler.setEncoding()
  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def logger(system:ActorSystem, arg:String):LoggingAdapter = {
    val log = Logging.getLogger(system, arg)

  return log

  }

  def logger(arg:String):Logger = {
    val log = Logger.getLogger(arg)

    return log

  }
  def __logger(arg:String):Logger = {
    var log = Logger.getLogger(arg)
    log.addHandler(handler)

    return log
  }
}
