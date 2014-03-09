import java.net.Socket
import server.lib.HttpRequest

/**
 * Created by hernansaab on 3/8/14.
 */
package object server {
  case class ClientSocketContainer(sock: Socket)
  case class TransactionConnectionContainer(request: HttpRequest)
}
