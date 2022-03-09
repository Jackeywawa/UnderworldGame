import scala.io.StdIn
import util.control.Breaks._
import scala.Console.{BOLD, RESET, UNDERLINED}
//import scala.util.Random
/*val random = new Random()
  val x = random.nextInt(10)*/

object characters extends App {
  val player = new toon()
  val boar = new boar(); val wolf = new wolf(); val harpy = new harpy(); val centaur = new centaur(); val minotaur = new minotaur(); val siren = new siren(); val lion = new lion(); val hydra = new hydra(); val dragon = new dragon(); val friend = new oldFriend()
  val enemies = List(boar, wolf, harpy, centaur, minotaur, siren, lion, hydra, dragon, friend)

  println("You are a Roman warrior who has just fallen in battle " +
    "and your spirit has been transferred to the underworld. Fight to survive!")
  GameplayLoop()

  def GameplayLoop(player: toon = player, enemies: List[toon] = enemies) : Unit = {
    while (player.isAlive) {
      for (i <- 0 to 10) {
        println(s"Your Stats: ${BOLD}Health: ${player.health}\t Attack: ${player.attack}\t Defense: ${player.defense}${RESET}")
        val checkUserFight = StdIn.readLine(s"Do you wish to fight ${enemies(i).name}? " +
          s"${BOLD}Health: ${enemies(i).health}\t Attack: ${enemies(i).attack}\t Defense: ${enemies(i).defense}${RESET}\n")
        if (checkUserFight.contains("y") || checkUserFight.contains("Y")) {
          fight(player, enemies(i))
        }
        else println(s"You have decided to run from ${enemies(i).name}")
      }
    }
  }

  def combat(attacker: toon, defender: toon) = { //combat sequence
    val damage = attacker.attack - defender.defense
    defender.battleHealth -= damage
    if (defender.battleHealth <= 0) defender.battleHealth = 0
    println(s" ${attacker.name} attacks!")
    print(s"${defender.name} takes $damage damage! ${defender.name} has ${defender.battleHealth} HP left!")
    if (defender.battleHealth <= 0) {
      println(s"\n${BOLD}${defender.name} has died!")
      defender.isAlive = false
      attacker.battleHealth = attacker.health
      if (attacker.name == "Player") {
        levelUp(attacker)
      }
      else {
        println(s"${BOLD} GAME OVER!")
        break()
      }
    }
  }
  def fight(attacker: toon, defender: toon) : Unit = { //turn based
    while (attacker.isAlive && defender.isAlive) {
      combat(attacker, defender)
      if(defender.isAlive) {
        combat(defender, attacker)
      }
    }
  }
  def levelUp(character: toon) = {
    character.level += 1
    character.attack += 1
    character.defense += 1
    character.health += 1
    println(s"${character.name} has leveled up!")
  }
}

class toon() {
  var isAlive = true;
  var health = 20; var attack = 10; var defense = 5; var level = 1
  var battleHealth = health
  var name = "Player"
}

class boar() extends toon {
  this.health = 10; this.attack = 7; this.defense = 2
  this.name = "Boar"
}
class wolf() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class harpy() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class centaur() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class minotaur() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class siren() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class lion() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class hydra() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class dragon() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}
class oldFriend() extends toon {
  this.health = 10; this.attack = 12; this.defense = 5
  this.name = "Wolf"
}