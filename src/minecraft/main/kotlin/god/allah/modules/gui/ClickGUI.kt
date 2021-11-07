package god.allah.modules.gui

import god.allah.api.clickgui.ClickGUI
import god.allah.api.event.Event
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import org.lwjgl.input.Keyboard

@ModuleInfo("ClickGUI", Category.GUI, defaultKey = Keyboard.KEY_RSHIFT)
class ClickGUI : Module() {
    override fun onEvent(event: Event) {

    }

    override fun onEnable() {
        mc.displayGuiScreen(ClickGUI())
    }

    override fun onDisable() {

    }

}
