import scala.io.StdIn
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

  //set up for gameplay loop
  var gameIsActive = true;
  val player = new toon()
  val boar = new boar(); val wolf = new wolf(); val harpy = new harpy(); val centaur = new centaur(); val minotaur = new minotaur(); val siren = new siren(); val lion = new lion(); val hydra = new hydra(); val dragon = new dragon(); val friend = new oldFriend()
  val enemies = List(boar, wolf, harpy, centaur, minotaur, siren, lion, hydra, dragon, friend)
  val goodEndingText = "\nAfter winning against your former comrade, you find yourself in a land with beautiful fields of grass, a lively sun, and a village in the distance. " +
    "\nIt was paradise compared to everything you have been through so far. You see a horse in the distance and decide to mount it to begin heading towards the village. " +
    "\nAs you are galloping, you remember a saying before you had died: " +
    "\n“If you find yourself alone, riding in green fields with the sun on your face, do not be troubled. For you are in Elysium, and you’re already dead!” " +
    "\nWith a smile on your face, you gallop into the sun, leaving all of your past troubles to be at peace at last.\n" +
    s"\n${BOLD}${UNDERLINED}THE END$RESET"
  val badEndingText = "Whether it was cowardice or weakness, you turn away from your last foe and chance of salvation. You slink back into the darkness, damning yourself to spend eternity in the Underworld.\n" +
    s"\n${BOLD}${UNDERLINED}THE END$RESET"


  GameplayLoop()

  connection.close()

  def GameplayLoop(player: toon = player, enemies: List[toon] = enemies, goodEnding: String = goodEndingText, badEnding: String = badEndingText) : Unit = {
    println("You are a Roman warrior who has just fallen in battle " +
      "and your spirit has been transferred to the underworld. Fight to survive!")
    while (gameIsActive) {
      for (i <- 0 until enemies.length-1) {
        println(s"\nYour Stats: ${BOLD}Health: ${player.health}\t Attack: ${player.attack}\t Defense: ${player.defense}$RESET")
        val checkUserFight = StdIn.readLine(s"A wild ${enemies(i).name} has appeared! Do you wish to fight?\n" +
          s"${BOLD}Health: ${enemies(i).health}\t Attack: ${enemies(i).attack}\t Defense: ${enemies(i).defense}$RESET\n")
        insertIntoAnswer(checkUserFight)
        if (checkUserFight.contains("y") || checkUserFight.contains("Y")) {
          fight(player, enemies(i))
        }
        else {
          println(s"You have decided to run from ${enemies(i).name}")
          insertIntoRemainingHP(player.health)
          insertIntoPlayerLevel(player.level)
        }
      }
      if (player.isAlive) {
        println("\nAfter encountering many hellish foes that you would only imagine in your worst nightmares, you see something strange in the distance.\n" +
          "It appears to be a gate of some sort, surrounded by land with signs of life that contrasted the bleakness of the Underworld.\n" +
          "Upon drawing closer, you realize it is the very same gate that led to your hometown in life and see a familiar face.\n" +
          "It is your best friend who had grown up with you, fought by your side for years, and ultimately stayed with you to the bitter end.\n" +
          "Suddenly, a voice fills your head: \"In order to reach paradise, you must slay the one who stands in your path.\"\n")
        println(s"\nYour Stats: ${BOLD}Health: ${player.health}\t Attack: ${player.attack}\t Defense: ${player.defense}${RESET}")
        val checkFinalFight = StdIn.readLine(s"${BOLD}Health: ${friend.health}\t Attack: ${friend.attack}\t Defense: ${friend.defense}${RESET}\n" +
          s"Will you fight this last battle?\n")
        insertIntoAnswer(checkFinalFight)
        if (checkFinalFight.contains("y") || checkFinalFight.contains("Y")) {
          fight(player, friend)
        }
        else {
          println(badEnding)
          player.isAlive = false
          insertIntoRemainingHP(player.health)
          insertIntoPlayerLevel(player.level)
        }
      }
      if (player.isAlive) {
        println(goodEnding)
      }
      gameIsActive = false
    }
  }

  def combat(attacker: toon, defender: toon): Unit = { //combat sequence
    val damage = attacker.attack - defender.defense
    defender.battleHealth -= damage
    if (defender.battleHealth <= 0) defender.battleHealth = 0     //prevents overkill value from being displayed
    println(s" ${attacker.name} attacks!")
    print(s"${defender.name} takes $damage damage! ${defender.name} has ${defender.battleHealth} HP left!")
    if (defender.battleHealth <= 0) {
      println(s"\n${BOLD}${defender.name} has died!")
      defender.isAlive = false
      //attacker.battleHealth = attacker.health   //reset health
      if (attacker.name == "Player") {
        insertIntoRemainingHP(attacker.battleHealth)
        levelUp(attacker)
      }
      else {
        insertIntoRemainingHP(defender.battleHealth)
        println(s"${BOLD}GAME OVER!$RESET")
        break() //Exception in thread "main" scala.util.control.BreakControl
      }
    }
  }
  def fight(attacker: toon, defender: toon) : Unit = { //turn based system
    while (attacker.isAlive && defender.isAlive) {
      combat(attacker, defender)
      if(defender.isAlive) {
        combat(defender, attacker)
      }
    }
  }
  def levelUp(character: toon): Unit = {
    character.level += 1
    character.attack += 1
    character.defense += 1
    character.health += 1
    character.battleHealth = character.health
    println(s"${character.name} has leveled up! All stats increased by 1")
    insertIntoPlayerLevel(character.level)
  }
  def insertIntoAnswer(answer: String) : Unit = {
    val insertIntoAnswer =
      s"""
         |insert into playerAnswers (answer) values (?)
         |""".stripMargin
    val preparedStmtLevel = connection.prepareStatement(insertIntoAnswer)
    preparedStmtLevel.setString(1, s"${answer}")
    //preparedStmtLevel.setString(2, s"${foreignId}")
    preparedStmtLevel.execute
  }
  def insertIntoPlayerLevel(pLevel: Int) : Unit = {
    val insertIntoLevel =
      s"""
         |insert into playerLevel (level) values (?)
         |""".stripMargin
    val preparedStmtLevel = connection.prepareStatement(insertIntoLevel)
    preparedStmtLevel.setString(1, s"${pLevel}")
    //preparedStmtLevel.setString(2, s"${foreignId}")
    preparedStmtLevel.execute
  }
  def insertIntoRemainingHP(remaining: Int) : Unit = {
    val insertIntoHP =
      s"""
         |insert into remainingHP (health) values (?)
         |""".stripMargin
    val preparedStmtHP = connection.prepareStatement(insertIntoHP)
    preparedStmtHP.setString(1, s"${remaining}")
    preparedStmtHP.execute
  }
  def printColumnsFromSQL () : Unit = {
    /*val resultSetMonsters = statement.executeQuery("SELECT * FROM monsters;")
    val resultSetLevel = statement.executeQuery("SELECT * FROM playerLevel;")
    val resultSetHP = statement.executeQuery("SELECT * FROM remainingHP;")
    while ( resultSetMonsters.next() ) { //prints columns in monsters
      println(resultSetMonsters.getString(1)+", " + resultSetMonsters.getString(2))
    }
    while ( resultSetHP.next() ) { //prints columns in playerLevel
      println(resultSetHP.getString(1) +", " + resultSetHP.getString(2))
    }
    while ( resultSetLevel.next() ) { //prints columns in remainingHP
      println(resultSetLevel.getString(1)+", " + resultSetLevel.getString(2))
    }*/
  }
}

//player class
class toon() {
  var isAlive = true;
  var health = 20; var attack = 10; var defense = 5; var level = 1
  var battleHealth = health
  var name = "Player"
}

//enemies inheriting properties from player class
class boar() extends toon {
  this.health = 10; this.attack = 7; this.defense = 2
  this.battleHealth = this.health
  this.name = "Boar"
}
class wolf() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.battleHealth = this.health
  this.name = "Wolf"
}
class harpy() extends toon {
  this.health = 15; this.attack = 15; this.defense = 5
  this.battleHealth = this.health
  this.name = "Harpy"
}
class centaur() extends toon {
  this.health = 20; this.attack = 19; this.defense = 5
  this.battleHealth = this.health
  this.name = "Centaur"
}
class minotaur() extends toon {
  this.health = 30; this.attack = 20; this.defense = 10
  this.battleHealth = this.health
  this.name = "Minotaur"
}
class siren() extends toon {
  this.health = 20; this.attack = 18; this.defense = 6
  this.battleHealth = this.health
  this.name = "Siren"
}
class lion() extends toon {
  this.health = 30; this.attack = 20; this.defense = 5
  this.battleHealth = this.health
  this.name = "Nemean Lion"
}
class hydra() extends toon {
  this.health = 40; this.attack = 15; this.defense = 10
  this.battleHealth = this.health
  this.name = "Lernaean Hydra"
}
class dragon() extends toon {
  this.health = 50; this.attack = 16; this.defense = 9
  this.battleHealth = this.health
  this.name = "Eldwyrm"
}
class oldFriend() extends toon {
  this.health = 30; this.attack = 20; this.defense = 10
  this.battleHealth = this.health
  this.name = "Old Friend"
}
