package model

import untitled.goose.framework.model.events.GameEvent

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
  private class FileQuestionManager(filePath: String*) extends QuestionManager {

    override def getQuestion(category: String, difficulty: String): Question = ???

    override def availableCategories: Seq[String] = ???

    override def availableDifficulties: Seq[String] = ???
  }

  /**
   * Generate a question manager parsing the given question file.
   * @param filePath a list of paths in the resources that identify the question files
   * @return a new question manager.
   */
  def apply(filePath: String*): QuestionManager = new FileQuestionManager(filePath:_*)
}
