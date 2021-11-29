package god.allah.events

import god.allah.api.event.Event

class MoveEvent(var yaw: Float, val type: Type) : Event() {
    enum class Type {
        JUMP, MOVE
    }
}
