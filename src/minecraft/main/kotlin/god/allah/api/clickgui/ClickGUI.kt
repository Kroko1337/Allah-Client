package god.allah.api.clickgui

import god.allah.api.Resolution
import god.allah.api.clickgui.list.Panel
import god.allah.api.executors.Category
import net.minecraft.client.gui.GuiScreen
import kotlin.math.max


class ClickGUI : GuiScreen() {
    private var panelArrayList = ArrayList<Panel>()
    private var inited = false
    private var lastWidth: Int = Resolution.width
    private var lastHeight: Int = Resolution.height

    init {
        for (category in Category.values()) {
            panelArrayList.add(Panel(20, 20, category))
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (lastWidth != Resolution.width || lastHeight != Resolution.height)
            inited = false
        if (!inited) {
            for (i in panelArrayList.indices) {
                val panel = panelArrayList[i]
                panel.extended = false
                var width = 0
                panel.modules.forEach { module ->
                    if (fontRenderer.getStringWidth(module.mod.name) + 10 > width) {
                        width = fontRenderer.getStringWidth(module.mod.name) + 10
                    }
                }
                panel.width = max(width, 90)
            }
            for (i in panelArrayList.indices) {
                val panel = panelArrayList[i]
                val nextPanel: Panel? = if (i + 1 < panelArrayList.size) panelArrayList[i + 1] else null
                if (nextPanel != null) {
                    val nextX = panel.x + panel.width + 10
                    nextPanel.x = nextX
                    nextPanel.y = panel.y
                    if (nextX + nextPanel.width >= Resolution.width) {
                        nextPanel.y += panel.height + 5
                        nextPanel.x = panelArrayList[0].x
                    }
                }
            }
            inited = true
        }

        for (panel in panelArrayList) {
            panel.drawPanel(mouseX, mouseY)
        }
        lastWidth = Resolution.width
        lastHeight = Resolution.height
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