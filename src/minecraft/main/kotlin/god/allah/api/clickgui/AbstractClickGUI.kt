package god.allah.api.clickgui

import god.allah.api.Wrapper

abstract class AbstractClickGUI {

    open fun calculateMiddle(text: String?, x: Int, width: Int): Float {
        return ((x + width).toFloat() - Wrapper.fr.getStringWidth(text) / 2f - width.toFloat() / 2)
    }

    var width: Int = 120
    var height: Int = 15

    abstract fun mouseReleased()

    open fun isOver(x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int): Boolean {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height
    }

    abstract fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int)

    open fun drawPanel(mouseX: Int, mouseY: Int) {}

    open fun drawModules(width: Int, height: Int, x: Int, y: Int) {}

}