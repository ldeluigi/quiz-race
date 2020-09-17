import scalafx.scene.paint.Color
import untitled.goose.framework.dsl.GooseDSL
import untitled.goose.framework.dsl.board.words.DispositionType.Ring
import untitled.goose.framework.model.events.CustomPlayerEvent
import untitled.goose.framework.model.rules.ruleset.PlayerOrderingType.Fixed

object QuizRace extends GooseDSL with QuizRaceUtils {

  Rules of "Quiz Race"
  2 to 4 players

  Players have order(Fixed)

  The game board has size(52)
  the game board has disposition(Ring)

  getTileGroups(52).foreach(e => {
    The tiles (e._2: _*) have group(e._1)
    All tiles e._1 have colour(getCategoryColor(e._1))
  })

  The tiles (getSpecialGroup(52, 7): _*) have group(specialGroup)

  Players start on tile 1

  Define playerEvent playerBetEvent having (
    "bet" as[Int] value
    )

  Each turn players are (
    always allowed to displayQuestion("Place a bet", "Choose how many steps you want to bet",
      "1" -> (customPlayerEvent(playerBetEvent, _.currentPlayer) :+ ("bet", _ => 1)),
      "2" -> (customPlayerEvent(playerBetEvent, _.currentPlayer) :+ ("bet", _ => 2)),
      "3" -> (customPlayerEvent(playerBetEvent, _.currentPlayer) :+ ("bet", _ => 3)),
      "4" -> (customPlayerEvent(playerBetEvent, _.currentPlayer) :+ ("bet", _ => 4)),
      "5" -> (customPlayerEvent(playerBetEvent, _.currentPlayer) :+ ("bet", _ => 5))
    ) as "Place a bet" priority 1)


  //The first to reach the last tile is the winner!


  //When a bet is placed show a question based on the tile on which the player is standing.
  // A correct answer makes you go forward of the bet amount, a wrong one makes you go backwards.
  always when numberOf(events[CustomPlayerEvent] matching (e => e.name == playerBetEvent)) is (_ > 0) resolve
    displayQuestion((e: CustomPlayerEvent, s) => {
      val currentTile = s.playerPieces(e.player).position.map(_.tile)
      getQuestion(getCategory(currentTile), getDifficulty(currentTile), e.getProperty[Int]("bet").get, e, s)
    }) andThen consume

  Include these system behaviours(
    MultipleStep,
    VictoryManager
  )



}
