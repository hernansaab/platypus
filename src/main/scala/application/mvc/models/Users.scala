package application.mvc.models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted
/**
 * Created by hernansaab on 2/27/14.
 */

case class User(id: Option[Long], firstName: String, lastName: String, email: String, street: String, city: String, zip: String, phone: String)
class Users(tag: Tag) extends Table[User](tag, "x_users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc, O.NotNull)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def email = column[String]("email")
  def street = column[String]("street")
  def city = column[String]("city")
  def zip = column[String]("zip")
  def phone = column[String]("phone")
  def * = (id.?, firstName, lastName, email, street, city, zip, phone) <> (User.tupled, User.unapply)
}
