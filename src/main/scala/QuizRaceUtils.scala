import model.QuestionManager
import untitled.goose.framework.model.entities.runtime.GameState
import untitled.goose.framework.model.events.GameEvent
import untitled.goose.framework.model.events.consumable.{ConsumableGameEvent, StepMovementEvent}

import scala.util.Random

trait QuizRaceUtils {

  private val questionManager : QuestionManager = QuestionManager(???)
  val specialGroup = "SpecialGroup"
  var tiles = Seq[Int]
  val playerBetEvent: String = "PlayerBetEvent"
  private val questionManager : QuestionManager = QuestionManager("./resources/questions")

  def getSpecialGroup(total: Int, numSpecialTile: Int): Seq[Int] = {
    (1 until numSpecialTile).map(_ => Random.nextInt(total))
    //metto il seed sul random
  }

  def getTileGroups(total: Int) : Map[String, Seq[Int]] = {
    val groupSize = total / questionManager.availableCategories.size
    ???
  }

  //TODO change with event
  def getQuestion(category: String, difficulty: String, bet: Int, event: ConsumableGameEvent, gameState: GameState):
  (String, String, Seq[(String, StepMovementEvent)]) = {
    ???
  }
}
