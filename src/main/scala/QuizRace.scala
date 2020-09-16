import untitled.goose.framework.dsl.GooseDSL
import untitled.goose.framework.model.events.consumable.ConsumableGameEvent
import untitled.goose.framework.model.rules.ruleset.PlayerOrderingType.Fixed
import untitled.goose.framework.dsl.board.words.DispositionType.Ring
import untitled.goose.framework.model.events.{CustomGameEvent, CustomPlayerEvent}

object QuizRace extends GooseDSL with QuizRaceUtils{

  Rules of "Quiz Race"
  2 to 4 players

  Players have order(Fixed)

  The game board has size(52)
  the game board has disposition(Ring)

  getTileGroups(52).foreach(e =>{
    The tiles(e._2:_*) have group(e._1)
  })

  The tiles(getSpecialGroup(52, 7):_*) have group(specialGroup)

  Players start on tile 1

  Define playerEvent playerBetEvent having(
    "bet" as[Int] value
    )

  //show question as plays a bet
  Each turn players are (
    always allowed to displayQuestion("Place a bet", "Choose how many steps you want to bet",
      "1"->(customPlayerEvent(playerBetEvent, _.currentPlayer):+("bet",_=> 1)),
      "2"->(customPlayerEvent(playerBetEvent, _.currentPlayer):+("bet",_=> 2)),
      "3"->(customPlayerEvent(playerBetEvent, _.currentPlayer):+("bet",_=> 3)),
      "4"->(customPlayerEvent(playerBetEvent, _.currentPlayer):+("bet",_=> 4)),
      "5"->(customPlayerEvent(playerBetEvent, _.currentPlayer):+("bet",_=> 5))
    ) as "Place a bet" priority 1 )

  //ad ogni turno
  //lanciare un dialog con 5 opzioni per scegliere quanto scommettere
  //le opzioni sono custom event "PlayerBetEvent"

  //"regola" che risponde ad un PlayerBetEvent(nome) dice che in base alla posizione del player seleziona una
  // domanda e la mostra a schermo [Playerevent] nel matching dico cheil suo nome è playerbetevent(faccio la var o uso la stringa)
  always when numberOf (events[CustomPlayerEvent] matching(e => e.name == playerBetEvent)) is (_>0) resolve (
    //displayQuestion((e,s) => getQuestion(???, ???,e.getProperty[Int]("bet"), e, s) )
    )andThen consume


  //la domanda ha tot opzioni di cui una corretta che lancia l'evento di andare avanti mentre le altre
  //lanciano l'evento di andare indietro (StepMovementEvent)


  //Controllare che chi arriva alla fine vince
}
