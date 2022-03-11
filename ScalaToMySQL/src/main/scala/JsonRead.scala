import java.sql.{Connection, DriverManager}
import scala.io.Source.fromFile
import scala.io.StdIn


object JsonRead extends App {
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
    println(s"${jsonFinal(i)} has been inserted into the monsters table.")
  }

  truncateMonsters()

  preparedStmt.close()
  connection.close()

  def truncateMonsters() : Unit = {
    val truncCheck = StdIn.readLine("Do you wish to truncate the Monsters table? Y/N")
    if (truncCheck.contains('Y') || truncCheck.contains('y')) {
      val truncateStr =
      s"""
         |truncate table monsters
         |""".stripMargin

      val truncMonstersStmt = connection.prepareStatement(truncateStr)
      truncMonstersStmt.execute
      truncMonstersStmt.close()
      println(s"Monsters Table has been truncated.")
    }
    else println("Truncate Aborted")
  }
}
