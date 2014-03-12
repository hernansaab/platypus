package server.lib

/**
 * Created by hernansaab on 3/8/14.
 */
class SingleTransaction(_header:String) {



  val header = if(_header == null) "" else _header

  val(command, path, argument, httpVersion) = Utils.parseGetCommand(header)
  val contentType = Utils.parseContentType(header)
  val contentEncoding = Utils.parseContentEncoding(header)
  val postSize = Utils.parsePostSize(header)
  val connectionType = Utils.parseConnectionType(header)
  var body:String = null
  var startTime = System.nanoTime()
  val isClosedTransaction:Boolean =
    if(_header == null || _header == "") true
    else false

  def setBody(_body:String){
    body = _body
  }
  object Utils {

    def parsePostSize(_header: String): Int = {
      val pattern = "Content-Length: (\\d+)".r
      var list = pattern.findAllIn(_header).matchData.map(m => m.group(1)).toList
      val size =
        if (list.size == 0) 0
        else list.head.toInt
      size
    }
    def parseGetCommand(header: String): (String, String, String, String) = {
      val FullCommand = "(?s)(GET|POST|PUT|DELETE|HEAD)\\s+([^\\?\\s]+)((\\?)?(\\S+)?)\\s+(\\S+).+".r
      try {
        val FullCommand(command, path, dummy1, dummy2, argument, httpVersion) = header
        (command, path, argument, httpVersion)
      } catch {
        case e: Exception => {
          ("", "", "", "")
        }
      }
    }
    def parseContentType(header: String): String = {
      val pattern = "Content-Type: (\\d+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList
      var contentType =
        if (list.size == 0) ""
        else list.head

      if(contentType == null)
         contentType = ""
      contentType
    }
    def parseContentEncoding(header: String): String = {
      val pattern = "Content-Encoding:\\s(.+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList
      val value =
        if (list.size == 0) ""
        else list.head
      value
    }
    def parseConnectionType(header: String): String = {
      val pattern = "Connection:\\s(.+)".r
      var list = pattern.findAllIn(header).matchData.map(m => m.group(1)).toList

      if (list.size == 0) "keep-alive"
      else list.head.toLowerCase()

    }

  }
}
