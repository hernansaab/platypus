package  server
import java.io.IOException
import java.net.{DatagramSocket, ServerSocket, Socket}
import lib._
import java.util.concurrent.LinkedBlockingQueue


/**
 * Created by hernansaab on 3/8/14.
 */
object server {
  case class ClientSocketContainer(request: HttpRequest)
  case class TransactionConnectionContainerWriter(request: HttpRequest)
  case class TransactionConnectionContainerReader(request: HttpRequest)
  case class ConnectionReadyWaiter(request: HttpRequest)
  case class Fire(worker: Int, workersQueue:LinkedBlockingQueue[HttpRequest])

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

