import model.QuestionManager
import model.events.PlayerScoreEvent
import untitled.goose.framework.model.Colour
import untitled.goose.framework.model.entities.runtime.GameStateExtensions._
import untitled.goose.framework.model.entities.runtime.{GameState, Player, Tile}
import untitled.goose.framework.model.events.{CustomPlayerEvent, GameEvent}

import scala.util.Random

trait QuizRaceUtils {

  val boardSize: Int = 52

  val special = "special"
  val colours = Seq(
    Colour("#FFC107"),
    Colour("#FF5722"),
    Colour("#D32F2F"),
    Colour("#8BC34A"),
    Colour("#00897B"),
    Colour("#00BCD4"),
    Colour("#673AB7"),
    Colour("#B39DDB")
  )

  val displayBetMenu: String = "DisplayBetMenu"
  val displayScore: String = "DisplayScore"
  val playerBetEvent: String = "PlayerBetEvent"

  val questionManager: QuestionManager = QuestionManager("questions/easyquestions", 8, "questions/difficulties.json")
  private val orderedCategories: Seq[String] = questionManager.availableCategories.toList

  def getSpecialGroup(total: Int, numSpecialTile: Int): Seq[Int] =
    (1 until numSpecialTile).map(_ => Random.nextInt(total))

  def tileGroups(total: Int): Map[String, Seq[Int]] = {
    val categories = orderedCategories
    (1 to total)
      .map(i => i % categories.length -> i)
      .map(e => categories(e._1) -> e._2)
      .groupBy(_._1)
      .map(e => e._1 -> e._2.map(_._2))
  }

  def getCategoryColor(category: String): Colour =
    colours(orderedCategories.indexOf(category))


  /**
   * Return the category of this tile, a random one if none present
   *
   * @param tile an option containing a tile
   * @return the category of this tile
   */
  def getCategory(tile: Option[Tile]): String = {
    val groups: Seq[String] = tile.map(_.definition.groups).getOrElse(Seq())
    val categories = groups.intersect(orderedCategories)
    if (categories.nonEmpty) categories.head else Random.shuffle(orderedCategories).head
  }

  def easy: Int = questionManager.availableDifficulties.head

  def hard: Int = questionManager.availableDifficulties.last

  def getQuestion(e: CustomPlayerEvent, s: GameState): (String, String, Seq[(String, GameEvent)]) = {
    val currentTile = s.playerPieces(e.player).position.map(_.tile)
    val q = questionManager.getQuestion(getCategory(currentTile), e.getProperty[Int]("difficulty").get)
    questionManager.burnQuestion(getCategory(currentTile), e.getProperty[Int]("difficulty").get)
    val options = q.options.map(o => (o._1, PlayerScoreEvent((if (o._2) 1 else -1) * e.getProperty[Int]("bet").get, s.currentPlayer, s.currentTurn, s.currentCycle))).toSeq
    (q.title, q.text, options)
  }


  def currentScore(player: Player): Int = {
    player.history.only[PlayerScoreEvent].map(_.score).sum
  }


  def isOnSpecialTile(s: GameState): Boolean = s.playerPieces(s.currentPlayer).position.exists(_.tile.definition.belongsTo(special))
}
