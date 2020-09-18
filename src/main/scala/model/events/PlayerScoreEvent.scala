package model.events

import untitled.goose.framework.model.entities.runtime.Player
import untitled.goose.framework.model.events.PlayerEvent
import untitled.goose.framework.model.events.consumable.ConsumableGameEvent

case class PlayerScoreEvent(score: Int, player: Player, turn: Int, cycle: Int) extends ConsumableGameEvent with PlayerEvent {
  override def toString(): String = super.toString() + "score: " + score
}

