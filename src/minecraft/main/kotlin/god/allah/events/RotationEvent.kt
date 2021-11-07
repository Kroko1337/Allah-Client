package god.allah.events

import god.allah.api.event.Event

class RotationEvent(var yaw: Float, var pitch: Float) : Event() {
}