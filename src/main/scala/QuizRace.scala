import untitled.goose.framework.dsl.GooseDSL
import untitled.goose.framework.model.events.CustomPlayerEvent
import untitled.goose.framework.model.events.consumable.ConsumableGameEvent

object QuizRace extends GooseDSL with QuizRaceUtils{


  //50 tile loop
  //calcolare i gruppi in base alle categorie presenti
  //settare i gruppi delle caselle come ritornati con il nome della categoria
  //"a mano" metti le caselle speciali
  //

  //ad ogni turno
  //lanciare un dialog con 5 opzioni per scegliere quanto scommettere
  //le opzioni sono custom event "PlayerBetEvent"

  //"regola" che risponde ad un PlayerBetEvent dice che in base alla posizione del player seleziona una
  // domanda e la mostra a schermo
  //la domanda ha tot opzioni di cui una corretta che lancia l'evento di andare avanti mentre le altre
  //lanciano l'evento di andare indietro (StepMovementEvent)


  //Controllare che chi arriva alla fine vince


  Define playerEvent "PlayerBetEvent" having(
    "bet" as[Int] value
  )

  //Note this is an empty template
  always when numberOf (events[CustomPlayerEvent] matching(e => true)) is (_>0) resolve (
    displayQuestion((e,s) => getQuestion(???, ???, e, s) )
  )andThen consume
}
