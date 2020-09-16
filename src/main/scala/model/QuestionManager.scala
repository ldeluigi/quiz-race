package model

import java.io.File

import scala.util.Random

/**
 * This trait represent an interface to ask questions of a specific category to a question manager
 * This should attempt to show always fresh questions.
 * If all questions have been shown it shuffles the deck and then restart.
 */
trait QuestionManager {
  /**
   * This methods retrieve a question of the given category and difficulty.
   * If there are no questions matching it will throw [[IllegalArgumentException]]
   * @param category the category chosen for the question.
   * @param difficulty the difficulty chosen for the question.
   * @return a new question
   */
  def getQuestion(category: String, difficulty: String): Question

  /**
   * This methods show all the categories available in this question manager.
   * @return a Seq containing the available categories.
   */
  def availableCategories : Seq[String]

  /**
   * This methods show the difficulties available for every category.
   * If some categories have different difficulties than others they will be ignored.
   * @return a Seq containing the available difficulties.
   */
  def availableDifficulties: Seq[String]
}


object QuestionManager {
  private class FileQuestionManager(folderPath: String) extends QuestionManager {

    var questionMap: Map[(String, String), Seq[Question]] = ???

    private def generateQuestionSeq : Seq[Question] = ???

    override def getQuestion(category: String, difficulty: String): Question = {
      val q = questionMap((category, difficulty)).head
      if(questionMap((category, difficulty)).tail.isEmpty) {
        questionMap += (category, difficulty) -> Random.shuffle(generateQuestionSeq)
      }
      q
    }

    override def availableCategories: Seq[String] = ???

    override def availableDifficulties: Seq[String] = ???
  }

  /**
   * Generate a question manager parsing the question files in a folder.
   * @param folderPath the path of a folder with the question files
   * @return a new question manager.
   */
  def apply(folderPath: String): QuestionManager = new FileQuestionManager(folderPath)
}
