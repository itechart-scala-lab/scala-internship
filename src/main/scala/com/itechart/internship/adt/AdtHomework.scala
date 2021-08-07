package com.itechart.internship.adt

object AdtHomework {

  // Implement ADTs for Roulette game

  // https://www.venetian.com/casino/table-games/roulette-basic-rules.html
  // https://www.mastersofgames.com/rules/roulette-rules.htm

  // Use material from adt workshop for implementation of the following data structures:

  /*
    - Number (1 - 36)
    - Color (Red or Black)
    - Bet Type (split, straight up, street, single number etc ...)
    - Player
    - GameResult
    - others if required
   */

  // Also you need to implement roulette engine methods

  final case class Player()
  final case class GameResult()

  // Is the function below a pure function?
  def generateNumber(): Int = ??? // Instead of Int it is better to provide other data structure

  def runGame(players: List[Player]): List[GameResult] = ???

  // add some tests via main or even unit tests to verify the game logic
}
