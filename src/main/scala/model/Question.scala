package model

import io.vertx.lang.scala.json.{JsonArray, JsonObject}
import org.apache.commons.text.StringEscapeUtils

trait Question {
  def category: String
  def difficulty: Int
  def title : String
  def text: String
  def options: Map[String, Boolean]

  override def toString: String =
    Seq(this.category, this.difficulty, this.title, this.text).mkString("Question: "," "," ") +
      options.toString()
}

object Question {
  case class JsonQuestion(private val jsonObject: JsonObject, private val difficulties: JsonArray) extends Question{
    override def category: String = jsonObject.getString("category")

    override def difficulty: Int =
      (for(i <- 0 until difficulties.size) yield difficulties.getString(i))
        .indexOf(jsonObject.getString("difficulty"))



    override def title: String = category + " question!"

    override def text: String = StringEscapeUtils.unescapeHtml4(jsonObject.getString("question"))

    override def options: Map[String, Boolean] = {
      val correct : String = StringEscapeUtils.unescapeHtml4(jsonObject.getString("correct_answer"))
      val array = jsonObject.getJsonArray("incorrect_answers")
      val incorrect: Seq[String] = for(i <- 0 until array.size) yield StringEscapeUtils.unescapeHtml4(array.getString(i))
      incorrect.map(s => (s, false)).toMap + (correct -> true)
    }
  }
}
