package application.mvc.models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted
import java.sql.Date
/**
 * Created by hernansaab on 2/27/14.
 */

case class Order(id: Option[Long], createdDate: Date, shippedDate: Date, status: String)
class Orders (tag: Tag) extends Table[Order](tag, "x_orders") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def createdDate = column[Date]("created_date")
  def shippedDate = column[Date]("shipped_date")
  def status = column[String]("status")
  def * = (id.?, createdDate, shippedDate, status) <> (Order.tupled, Order.unapply)
}
