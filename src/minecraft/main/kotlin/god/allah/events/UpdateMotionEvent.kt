package god.allah.events

import god.allah.api.event.Event

class UpdateMotionEvent(val state: State, var yaw: Float, var pitch: Float) : Event() {
}

enum class State {
    PRE, POST
}