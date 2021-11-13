package god.allah.api.helper

import god.allah.api.Wrapper

object PlayerHandler {
    var yaw: Float = 0.0F
    var pitch: Float = 0.0F
    var prevPitch: Float = 0.0F

    var currentItem: Int = 0

    fun hasAlready(slot: Int) : Boolean {
        return currentItem == slot
    }

    fun hasSilent() : Boolean {
        return currentItem != Wrapper.player.inventory.currentItem
    }
}