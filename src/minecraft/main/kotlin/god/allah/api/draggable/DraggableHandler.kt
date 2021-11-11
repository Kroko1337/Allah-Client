package god.allah.api.draggable

import god.allah.api.Registry
import god.allah.api.executors.Draggable

object DraggableHandler {

    fun handleMouseInput(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0 && !hasAlreadyDragged())
            Registry.getEntries(Draggable::class.java).forEach {
                if (mouseX >= it.hitBoxX && mouseX <= it.hitBoxX + it.width && mouseY >= it.hitBoxY && mouseY <= it.hitBoxY + it.height)
                    it.dragged = true
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
            it.xPos = mouseX
            it.yPos = mouseY
        }
    }

    private fun hasAlreadyDragged() : Boolean {
        return Registry.getEntries(Draggable::class.java).any { it.dragged }
    }

}