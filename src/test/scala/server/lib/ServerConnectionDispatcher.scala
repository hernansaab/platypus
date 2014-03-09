import org.scalatest._
import application.mvc.models._
import scala.slick.driver.JdbcProfile
import scala.slick.lifted.TableQuery
import scala.slick.driver.MySQLDriver.simple._


/**
 * Created by hernansaab on 3/2/14.
 */
class ServerConnectionDispatcher  extends FunSuite{
//Date.valueOf("2011-04-01")

  test("User Insertions") {
    val users = TableQuery[Users]
    Database.forURL("jdbc:mysql://localhost:3306/shopping",
      driver = "com.mysql.jdbc.Driver",
      user = "root",
      password = "myroot") withSession {
      session: Session =>
        implicit val s = session
        //coffees.map(c => (c.name, c.supID, c.price)) += ("Colombian_Decaf", 101, 8.99)

        users.map(c => (c.firstName, c.lastName, c.email, c.street, c.city, c.zip, c.phone)) += ("hernan", "saab", "hernan.saab@gmail.com", "3131 homestead road apt 10d", "santa clara", "95199", "650-248-4393")
//        users.insert(new User(None, "hernan", "saab", "hernan.saab@gmail.com", "3131 homestead road apt 10d", "santa clara", "95199", "650-248-4393"))

        val statement = users.insertStatement
        val invoker = users.insertInvoker
        assert(true)
    }
    //users.

  //  users.insert(new User(firstName:"hernan", "saab", "hernan.saab@gmail.com", "3131 homestead road apt 10d", "santa clara", "95199", "650-248-4393"))
    //(id, firstName, lastName, email, street, city, zip, phone)
//    users +=  (1, "hernan", "saab", "hernan.saab@gmail.com", "3131 homestead road apt 10d", "santa clara", "95199", "650-248-4393")

  }
}
