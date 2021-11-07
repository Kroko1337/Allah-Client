package god.allah.events

import god.allah.api.event.Event

class HandlePosLookEvent(var x: Double, var y: Double, var z: Double, var yaw: Float, var pitch: Float) : Event(){
}