import model.QuestionManager
import scalafx.scene.paint.Color
import untitled.goose.framework.model.entities.runtime.{GameState, Tile}
import untitled.goose.framework.model.events.CustomPlayerEvent
import untitled.goose.framework.model.events.consumable.StepMovementEvent

import scala.util.Random

trait QuizRaceUtils {

  val specialGroup = "SpecialGroup"
  val playerBetEvent: String = "PlayerBetEvent"


  private val questionManager: QuestionManager = QuestionManager("./resources/questions", "./resources/difficulties.json")

  def getSpecialGroup(total: Int, numSpecialTile: Int): Seq[Int] =
    (1 until numSpecialTile).map(_ => Random.nextInt(total))

  def getTileGroups(total: Int): Map[String, Seq[Int]] = {
    val categories = questionManager.availableCategories.toArray
    (1 to total)
      .map(i => i % categories.length -> i)
      .map(e => categories(e._1)-> e._2)
      .groupBy(_._1)
      .map(e => e._1 -> e._2.map(_._2))
  }


  def getCategoryColor(category: String): Color = Color.Red
  /**
   * Return the category of this tile, a random one if none present
   *
   * @param tile an option containing a tile
   * @return the category of this tile
   */
  def getCategory(tile: Option[Tile]): String = {
    val groups: Seq[String] = tile.map(_.definition.groups).getOrElse(Seq())
    val categories = groups.intersect(questionManager.availableCategories.toSeq)
    if (categories.nonEmpty) categories.head else Random.shuffle(questionManager.availableCategories).head
  }

  def getDifficulty(tile: Option[Tile]): Int = questionManager.availableDifficulties.head //TODO tile dependent?

  def getQuestion(category: String, difficulty: Int, bet: Int, e: CustomPlayerEvent, s: GameState):
  (String, String, Seq[(String, StepMovementEvent)]) = {
    val q = questionManager.getQuestion(category, difficulty)
    (q.title, q.text, Random.shuffle(q.options.toSeq).map(o =>
      (o._1, if (o._2)
        StepMovementEvent(bet, e.player, s.currentTurn, s.currentCycle) else
        StepMovementEvent(-bet, e.player, s.currentTurn, s.currentCycle))))
  }
}
