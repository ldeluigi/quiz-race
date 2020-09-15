import untitled.goose.framework.dsl.GooseDSL
import untitled.goose.framework.model.events.consumable.ConsumableGameEvent

object QuizRace extends GooseDSL with QuizRaceUtils{


  //Note this is an empty template
  always when numberOf (events[ConsumableGameEvent] matching(e => true)) is (_>0) resolve (
    displayQuestion((e,s) => getQuestion(???, ???, e, s) )
  )andThen consume
}
