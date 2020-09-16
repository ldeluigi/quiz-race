import java.io.File

import io.vertx.lang.scala.json.{Json, JsonArray, JsonObject}
import model.Question.JsonQuestion
import model.QuestionManager

import scala.io.Source

object FileTest extends App {

  val questionManager : QuestionManager = QuestionManager("./resources/questions", "./resources/difficulties.json")

  println(questionManager.availableCategories)
  println(questionManager.availableDifficulties)
  questionManager.allQuestions.foreach(println)

}


