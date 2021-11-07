package god.allah.events

import god.allah.api.event.Event

class KnockBackModifierEvent(var flag: Boolean, var motion: Double, var sprint: Boolean) : Event() {
}