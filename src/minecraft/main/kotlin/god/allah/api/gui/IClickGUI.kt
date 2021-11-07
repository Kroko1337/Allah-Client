package god.allah.api.gui

import god.allah.api.Resolution
import net.minecraft.client.gui.GuiScreen

abstract class IClickGUI(var panelWidth: Double, var panelHeight: Double) : GuiScreen() {
    abstract fun init()

    abstract fun drawPanel(resolution: Resolution)
}