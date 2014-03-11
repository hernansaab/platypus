import java.io.IOException
import java.net.{DatagramSocket, ServerSocket, Socket}
import server.lib.HttpRequest

/**
 * Created by hernansaab on 3/8/14.
 */
package object server {
  case class ClientSocketContainer(sock: Socket)
  case class TransactionConnectionContainer(request: HttpRequest)



  def isPortAvailable(port: Int): Boolean = {
    if (port < 10 || port > 65533) {
      throw new IllegalArgumentException("Invalid start port: " + port)
    }
    var ss: ServerSocket = null
    var ds: DatagramSocket = null
    try {
      ss = new ServerSocket(port)
      ss.setReuseAddress(true)
      ds = new DatagramSocket(port)
      ds.setReuseAddress(true)
      return true
    } catch {
      case e: IOException =>
    } finally {
      if (ds != null) {
        ds.close()
      }
      if (ss != null) {
        try {
          ss.close()
        } catch {
          case e: IOException =>
        }
      }
    }
    false
  }
}

