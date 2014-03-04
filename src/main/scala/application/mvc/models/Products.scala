package application.mvc.models
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created by hernansaab on 2/27/14.
 */
class Products(tag: Tag) extends Table[(Double, String, Double, Double, Double, Double, Double)](tag, "x_products") {
  def id = column[Double]("id", O.PrimaryKey, O.AutoInc)
  def description = column[String]("description")
  def width = column[Double]("width")
  def length = column[Double]("length")
  def height = column[Double]("height")
  def weight = column[Double]("weight")
  def value = column[Double]("value")

  def * = (id, description, width, length, height, weight, value)
}
