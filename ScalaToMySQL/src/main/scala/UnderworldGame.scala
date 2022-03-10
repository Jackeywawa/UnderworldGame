import scala.io.StdIn
import scala.io.Source.fromFile
import util.control.Breaks._
import scala.Console.{BOLD, RESET, UNDERLINED}
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException, Statement}


object UnderworldGame extends App{
  //setting up the connection to SQL
  val driver = "com.mysql.cj.jdbc.Driver"
  val url = "jdbc:mysql://localhost:3306/test"
  val username = "root"
  val password = "Jhintowin1"
  val connection:Connection = DriverManager.getConnection(url, username, password)
  val statement = connection.createStatement()

  //Reading and converting json content to a string
  val filePath = "src/test/scala/data.json"
  val json_toString = fromFile(filePath).mkString
  val json_shorten1 = json_toString.replace("\"", "")
  val json_shorten2 = json_shorten1.substring(14,81)
  val jsonFinal= json_shorten2.split(",").toList

  //loop for writing converted json string to SQL
  val insertIntoMonsters =
    s"""
       |insert into monsters (name) values (?)
       |""".stripMargin
  val preparedStmt = connection.prepareStatement(insertIntoMonsters)
  for (i <- 0 until jsonFinal.length) {
    preparedStmt.setString(1,s"${jsonFinal(i)}")
    preparedStmt.execute
  }


  preparedStmt.close()
  connection.close()

  def printColumns () : Unit = {
    val resultSet = statement.executeQuery("SELECT * FROM monsters;")
    while ( resultSet.next() ) { //prints ever
      println(resultSet.getString(1)+", " +resultSet.getString(2))
    }
  }
  /*def insertMonsters (jsonString: String) : Unit = {
    val insertSQL =
      s"""
        |insert into monsters (name) values (?)
        |""".stripMargin
    val preparedStmt = connection.prepareStatement(insertSQL)
    preparedStmt.setString(1, s"$jsonString")
    preparedStmt.execute
    preparedStmt.close()
  }*/

  /*def jsonToSQL(filePath: String) : Unit = {
    val json_toString = fromFile(filePath).mkString
    val json_shorten1 = json_toString.replace("\"", "")
    val json_shorten2 = json_shorten1.substring(14,81)
    val jsonFinal= json_shorten2.split(",").toList
  }*/
}