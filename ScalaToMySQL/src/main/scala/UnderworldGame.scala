import scala.io.StdIn
import scala.io.Source.fromFile
import util.control.Breaks._
import scala.Console.{BOLD, RESET, UNDERLINED}
import java.sql.{Connection, DriverManager, PreparedStatement, SQLException, Statement}


object UnderworldGame extends App{
  val driver = "com.mysql.cj.jdbc.Driver"
  val url = "jdbc:mysql://localhost:3306/test"
  val username = "root"
  val password = "Jhintowin1"
  val connection:Connection = DriverManager.getConnection(url, username, password)
  val statement = connection.createStatement()

  val filePath = "src/test/scala/data.json"
  val json_toString = fromFile(filePath).mkString
  val json_shorten1 = json_toString.replace("\"", "")
  val json_shorten2 = json_shorten1.substring(14,81)
  val jsonFinal= json_shorten2.split(",").toList

  val insertIntoMonsters =
    s"""
       |insert into monsters (name) values (?)
       |""".stripMargin
  val preparedStmt = connection.prepareStatement(insertIntoMonsters)
  for (i <- 0 until jsonFinal.length) {         //
    preparedStmt.setString(1,s"${jsonFinal(i)}")
    preparedStmt.execute
  }

  //printColumns()
  preparedStmt.close()
  connection.close()

  def printColumns () : Unit = {
    val resultSet = statement.executeQuery("SELECT * FROM playerlevel;")
    while ( resultSet.next() ) { //prints ever
      println(resultSet.getString(1)+", " +resultSet.getString(2))
    }
  }
  def insertMonsters (jsonString: String) : Unit = {
    val insertSQL =
      s"""
        |insert into monsters (name) values (?)
        |""".stripMargin
    val preparedStmt = connection.prepareStatement(insertSQL)
    preparedStmt.setString(1, s"$jsonString")
    preparedStmt.execute
    preparedStmt.close()
  }

  def jsonToSQL(filePath: String) : Unit = {
    val json_toString = fromFile(filePath).mkString
    val json_shorten1 = json_toString.replace("\"", "")
    val json_shorten2 = json_shorten1.substring(14,81)
    val jsonFinal= json_shorten2.split(",").toList
  }
}

/*
  1. Have a JSON file with variables of
  2. Read the contents into the program and store their values.
  3. Create 3 Tables in MySQL: Songs, Bands, and UserInput. Have Bands and UserInput linked to Songs
  4. Save the contents of the JSON files into their respective tables in MySQL
  5. Loop through the songs and band arrays, display to the user the song + band,
     ask and take input (Rating of 1-5)

 */