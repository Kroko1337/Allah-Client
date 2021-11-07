package god.allah.api.clickgui.list.underlist

import god.allah.api.clickgui.AbstractClickGUI
import god.allah.api.executors.Module
import god.allah.main.Wrapper
import net.minecraft.client.gui.Gui

class ModuleRendering(var mod: Module) : AbstractClickGUI() {

    var x: Int = 0
    var y: Int = 0

    override fun drawModules(width: Int, height: Int, x: Int, y: Int) {
        this.x = x
        this.y = y
        Gui.drawRect(x, y - 1, x + width, y + height - 1, Int.MIN_VALUE)

        System.out.println(mod)
        if (mod.isToggled()) {
            Gui.drawRect(x, y - 1, x + width, y + height - 1, Int.MIN_VALUE)
        }

        Wrapper.fr.drawStringWithShadow(mod.name, calculateMiddle(mod.name, x, width), (y + height - 12).toFloat(), -1)
        super.drawModules(width, height, x, y)
    }


    override fun mouseReleased() {
    }


    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val over = isOver(x, y, width, height, mouseX, mouseY)
        if (over) {
            if (mouseButton == 0) {
                mod.setToggled(!mod.isToggled())
            }
        }

    }


}