package model

trait Question {

  def title : String
  def text: String
  def options: Map[String, Boolean]
}
