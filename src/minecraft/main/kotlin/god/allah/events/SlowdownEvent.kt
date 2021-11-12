package god.allah.events

import god.allah.api.event.Event

class SlowdownEvent(var slowdown: Float) : Event() {
}