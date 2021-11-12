package god.allah.api.draggable

import god.allah.api.Registry
import god.allah.api.Resolution
import god.allah.api.executors.Draggable
import net.minecraft.util.math.MathHelper

object DraggableHandler {

    var dragX = 0.0
    var dragY = 0.0

    fun handleMouseInput(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0 && !hasAlreadyDragged())
            Registry.getEntries(Draggable::class.java).forEach {
                if (mouseX >= it.hitBoxX && mouseX <= it.hitBoxX + it.width && mouseY >= it.hitBoxY && mouseY <= it.hitBoxY + it.height) {
                    it.dragged = true
                    dragX = it.xPos.toDouble() - mouseX
                    dragY = it.yPos.toDouble() - mouseY
                }
            }
    }

    fun handleMouseRelease() {
        Registry.getEntries(Draggable::class.java).filter { it.dragged }.forEach {
            it.dragged = false
            it.savePositions()
        }
    }

    fun onUpdate(mouseX: Int, mouseY: Int) {
        Registry.getEntries(Draggable::class.java).filter { it.dragged }.forEach {
            val nextPosX = (mouseX + dragX).toInt()
            val nextPosY = (mouseY + dragY).toInt()

            it.xPos = MathHelper.clamp(nextPosX, 0, Resolution.width)
            it.yPos = MathHelper.clamp(nextPosY, 0, Resolution.height)
            it.hitBoxX = it.xPos
            it.hitBoxY = it.yPos
        }
    }

    private fun hasAlreadyDragged() : Boolean {
        return Registry.getEntries(Draggable::class.java).any { it.dragged }
    }

}