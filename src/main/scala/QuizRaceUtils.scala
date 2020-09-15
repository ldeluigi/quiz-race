import model.QuestionManager
import untitled.goose.framework.model.entities.runtime.GameState
import untitled.goose.framework.model.events.GameEvent
import untitled.goose.framework.model.events.consumable.ConsumableGameEvent

trait QuizRaceUtils {

  private val questionManager : QuestionManager = QuestionManager(???)

  //TODO change with event
  def getQuestion(category: String, difficulty: String, event: ConsumableGameEvent, gameState: GameState):
  (String, String, Seq[(String, GameEvent)]) = {
    ???
  }
}
