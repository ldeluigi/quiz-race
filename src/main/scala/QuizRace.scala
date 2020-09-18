import model.events.PlayerScoreEvent
import untitled.goose.framework.dsl.GooseDSL
import untitled.goose.framework.dsl.board.words.DispositionType.Ring
import untitled.goose.framework.model.events.consumable.{StepMovementEvent, TileEnteredEvent}
import untitled.goose.framework.model.events.{CustomGameEvent, CustomPlayerEvent}
import untitled.goose.framework.model.rules.ruleset.PlayerOrderingType.Fixed

object QuizRace extends GooseDSL with QuizRaceUtils {

  Rules of "Quiz Race"
  1 to 6 players

  Players have order(Fixed)

  The game board has size(boardSize)
  the game board has disposition(Ring)

  tileGroups(boardSize) foreach (e => {
    The tiles (e._2: _*) have group(e._1)
    All tiles e._1 have colour(getCategoryColor(e._1))
  })

  The tiles (getSpecialGroup(boardSize, 8): _*) have group(special)
  All tiles special have background("special.png")

  The tile boardSize has name("The end")

  Players start on tile 1

  Define event displayBetMenu

  Define event displayScore

  Define playerEvent playerBetEvent having(
    "bet" as[Int] value,
    "difficulty" as[Int] value
  )

  Each turn players are(
    always allowed to trigger customGameEvent(displayBetMenu) as "Place a bet" priority 1,
    always allowed to trigger customGameEvent(displayScore) as "Show score" priority 1,
  )


  always when numberOf(events[CustomGameEvent] matching (e => e.name == displayScore)) is (_ > 0) resolve (
    forEach displayMessage ((_, s) => ("Current score",
      "" + s.players.map(p => (p.name, currentScore(p)))
        .sortBy(p => p._2)
        .map(p => p._1 + "\t" + p._2 + " points")
        .mkString("\n")
    ))
    ) andThen consume

  //display the bet menu for hard tiles
  when(isOnSpecialTile) and numberOf(events[CustomGameEvent] matching (e => e.name == displayBetMenu)) is (_ > 0) resolve (
    forEach displayCustomQuestion((e, s) => ("Place a bet", "Choose how many steps you want to bet"),
      ((e, s) => "1", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 1) :+ ("difficulty", (e, s) => hard)),
      ((e, s) => "5", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 5) :+ ("difficulty", (e, s) => hard)),
      ((e, s) => "10", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 10) :+ ("difficulty", (e, s) => hard)),
      ((e, s) => "15", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 15) :+ ("difficulty", (e, s) => hard))
    )
    ) andThen consume

  //Display the bet menu for common tiles
  always when numberOf(events[CustomGameEvent] matching (e => e.name == displayBetMenu)) is (_ > 0) resolve (
    forEach displayCustomQuestion((e, s) => ("Place a bet", "Choose how many steps you want to bet"),
      ((e, s) => "1", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 1) :+ ("difficulty", (e, s) => easy)),
      ((e, s) => "2", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 2) :+ ("difficulty", (e, s) => easy)),
      ((e, s) => "3", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 3) :+ ("difficulty", (e, s) => easy)),
      ((e, s) => "4", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 4) :+ ("difficulty", (e, s) => easy)),
      ((e, s) => "5", playerEvent[CustomGameEvent](playerBetEvent, _.currentPlayer) :+ ("bet", (e, s) => 5) :+ ("difficulty", (e, s) => easy))
    )
    ) andThen consume

  //The first to reach the last tile is the winner!
  always when numberOf(events[TileEnteredEvent] matching
    (e => e.tile.definition.name.isDefined && e.tile.definition.name.get == "The end")) is (_ > 0) resolve
    trigger((_, s) => Victory(s)) andThen consume

  //When a bet is placed show a question based on the tile on which the player is standing.
  // A correct answer makes you go forward of the bet amount, a wrong one makes you go backwards.
  always when numberOf(events[CustomPlayerEvent] matching (e => e.name == playerBetEvent)) is (_ > 0) resolve (
    forEach displayQuestion ((e, s) => getQuestion(e, s))
    ) andThen consume

  //on special tiles you cannot go backwards but you could stay stuck
  when(isOnSpecialTile) and numberOf(events[PlayerScoreEvent] matching (_ => true)) is (_ > 0) resolve (
    forEach trigger ((e, s) => StepMovementEvent(if(e.score < 0) 0 else e.score, e.player, s.currentTurn, s.currentCycle))
  ) andThen consume && save


  always when numberOf(events[PlayerScoreEvent] matching (_ => true)) is (_ > 0) resolve (
    forEach trigger ((e, s) => StepMovementEvent(e.score, e.player, s.currentTurn, s.currentCycle))
    ) andThen consume && save

  Include these system behaviours(
    MultipleStep,
    VictoryManager
  )


}
