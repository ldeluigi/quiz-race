package model

import io.vertx.lang.scala.json.{Json, JsonArray}
import model.Question.JsonQuestion

import scala.io.Source
import scala.util.Random

/**
 * This trait represent an interface to ask questions of a specific category to a question manager
 * This should attempt to show always fresh questions.
 * If all questions have been shown it shuffles the deck and then restart.
 */
trait QuestionManager {
  /**
   * This methods retrieve the first available question of the given category and difficulty.
   * If there are no questions matching it will throw [[IllegalArgumentException]]
   *
   * @param category   the category chosen for the question.
   * @param difficulty the difficulty chosen for the question.
   * @return a question
   */
  def getQuestion(category: String, difficulty: Int): Question

  /**
   * This methods will burn the first available question of the given category and difficulty.
   *
   * @param category   the category chosen for the question.
   * @param difficulty the difficulty chosen for the question.
   *
   */
  def burnQuestion(category: String, difficulty: Int)

  /**
   * This methods show all the categories available in this question manager.
   *
   * @return a Set containing the available categories.
   */
  def availableCategories: Set[String]

  /**
   * This methods show the difficulties available for every category.
   *
   * @return a Seq containing the available difficulties.
   */
  def availableDifficulties: Seq[Int]

  def allQuestions: Seq[Question]
}


object QuestionManager {

  private class FileQuestionManager(root: String, n: Int, difficultiesPath: String) extends QuestionManager {

    private val difficultiesFile = Source.fromResource(difficultiesPath)
    private val difficulties: JsonArray = Json.fromArrayString(difficultiesFile.getLines().mkString("\n"))
    difficultiesFile.close()

    val allQuestions: Seq[Question] = (1 to n).map(root + "/" + _ + ".json").flatMap(fileName => {
      val file = Source.fromResource(fileName)
      val lines: String = file.getLines().mkString
      val questions: JsonArray = Json.fromObjectString(lines).getJsonArray("results")
      for (i <- 0 until questions.size) yield questions.getJsonObject(i)
    }).map(JsonQuestion(_, difficulties))

    var questionMap: Map[(String, Int), Seq[Question]] =
      allQuestions.groupBy(q => (q.category, q.difficulty)).map(q => (q._1, Random.shuffle(q._2)))

    private def generateQuestionSeq(category: String, difficulty: Int): Seq[Question] =
      allQuestions.filter(q => q.category == category && q.difficulty == difficulty)

    override def getQuestion(category: String, difficulty: Int): Question =
      questionMap((category, difficulty)).head


    override def availableCategories: Set[String] = questionMap.keySet.map(_._1)

    override def availableDifficulties: Seq[Int] = questionMap.keySet.map(_._2).toSeq.sorted

    override def burnQuestion(category: String, difficulty: Int): Unit =
      if (questionMap((category, difficulty)).tail.isEmpty) {
        questionMap += (category, difficulty) -> Random.shuffle(generateQuestionSeq(category, difficulty))
      } else {
        questionMap += (category, difficulty) -> questionMap(category, difficulty).tail
      }

  }

  /**
   * Generate a question manager parsing the question files in a folder.
   *
   * @return a new question manager.
   */
  def apply(root: String, n: Int, difficultiesPath: String): QuestionManager = new FileQuestionManager(root, n, difficultiesPath)
}
