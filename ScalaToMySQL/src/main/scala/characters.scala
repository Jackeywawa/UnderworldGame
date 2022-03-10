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
  val endingText = "After winning against your former comrade, you find yourself in a land with beautiful fields of grass, a lively sun, and a village in the distance. " +
    "\nIt was paradise compared to everything you have been through so far. You see a horse in the distance and decide to mount it to begin heading towards the village. " +
    "\nAs you are galloping, you remember a saying before you had died: " +
    "\n“If you find yourself alone, riding in green fields with the sun on your face, do not be troubled. For you are in Elysium, and you’re already dead!” " +
    "\nWith a smile on your face, you gallop into the sun, leaving all of your past troubles to be at peace at last." +
    "\nTHE END"

  println("You are a Roman warrior who has just fallen in battle " +
    "and your spirit has been transferred to the underworld. Fight to survive!")
  GameplayLoop()

  def GameplayLoop(player: toon = player, enemies: List[toon] = enemies, endingText: String = endingText) : Unit = {
    while (player.isAlive) {
      for (i <- 0 until enemies.length) {
        println(s"\nYour Stats: ${BOLD}Health: ${player.health}\t Attack: ${player.attack}\t Defense: ${player.defense}${RESET}")
        val checkUserFight = StdIn.readLine(s"A wild ${enemies(i).name} has appeared! Do you wish to fight?\n" +
          s"${BOLD}Health: ${enemies(i).health}\t Attack: ${enemies(i).attack}\t Defense: ${enemies(i).defense}${RESET}\n")
        if (checkUserFight.contains("y") || checkUserFight.contains("Y")) {
          fight(player, enemies(i))
        }
        else println(s"You have decided to run from ${enemies(i).name}")
      }
      player.isAlive = false;
    }
    println(endingText)
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
      //add remaining health insert to sql here
      attacker.battleHealth = attacker.health   //reset health
      if (attacker.name == "Player") {
        levelUp(attacker)
      }
      else {
        println(s"${BOLD}GAME OVER!")
        break() //Exception in thread "main" scala.util.control.BreakControl
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
    character.battleHealth = character.health
    println(s"${character.name} has leveled up! All stats increased by 1")
    //add level insert to here
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
  this.health = 20; this.attack = 18; this.defense = 3
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
  this.health = 50; this.attack = 16; this.defense = 10
  this.battleHealth = this.health
  this.name = "Eldwyrm"
}
class oldFriend() extends toon {
  this.health = 60; this.attack = 20; this.defense = 15
  this.battleHealth = this.health
  this.name = "Old Friend"
}