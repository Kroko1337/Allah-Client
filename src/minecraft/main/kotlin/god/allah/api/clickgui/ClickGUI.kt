package god.allah.api.clickgui

import god.allah.api.clickgui.list.Panel
import god.allah.api.executors.Category
import net.minecraft.client.gui.GuiScreen


class ClickGUI : GuiScreen() {
    private var panelArrayList =  ArrayList<Panel>()
    init {
        var xAxis = 0
        for (category in Category.values()) {
            panelArrayList.add(Panel(20 + xAxis, 20, category))
            xAxis += 70
        }
    }
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        for (panel in panelArrayList) {
            panel.drawPanel(mouseX, mouseY)
        }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        for (panel in panelArrayList) {
            panel.mouseClicked(mouseX, mouseY, mouseButton)
        }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        for (panel in panelArrayList) {
            panel.mouseReleased()
        }
        super.mouseReleased(mouseX, mouseY, state)
    }

}