package god.allah.api.draggable

import god.allah.api.Registry
import god.allah.api.Resolution
import god.allah.api.executors.Draggable
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

object DraggableHandler {

    var dragX = 0.0
    var dragY = 0.0
    var lastDistanceX = 0.0
    var lastDistanceY = 0.0

    fun handleMouseInput(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0 && !hasAlreadyDragged())
            Registry.getEntries(Draggable::class.java).forEach {
                if (it.isVisible() && mouseX >= it.hitBoxX && mouseX <= it.hitBoxX + it.width && mouseY >= it.hitBoxY && mouseY <= it.hitBoxY + it.height) {
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
            if (it.isVisible()) {
                val distanceX = (it.hitBoxX.toDouble() - it.xPos)
                val distanceY = (it.hitBoxY.toDouble() - it.yPos)

                if (lastDistanceX > distanceX)
                    dragX -= distanceX
                else if (lastDistanceX < distanceX)
                    dragX += lastDistanceX
                if (lastDistanceY > distanceY)
                    dragY -= distanceY
                else if (lastDistanceY < distanceY)
                    dragY += lastDistanceY

                val nextPosX = (mouseX + dragX).toInt()
                val nextPosY = (mouseY + dragY).toInt()

                it.xPos = MathHelper.clamp(nextPosX, 0, Resolution.width - (it.width - abs(it.xPos - it.hitBoxX)))
                it.yPos = MathHelper.clamp(nextPosY, 0, Resolution.height - (it.height - abs(it.yPos - it.hitBoxY)))
                it.hitBoxX = it.xPos
                it.hitBoxY = it.yPos

                lastDistanceX = distanceX
                lastDistanceY = distanceY
            }
        }
    }

    private fun hasAlreadyDragged(): Boolean {
        return Registry.getEntries(Draggable::class.java).any { it.dragged }
    }

}