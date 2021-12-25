package god.allah.modules.gui

import god.allah.api.Registry
import god.allah.api.Wrapper
import god.allah.api.clickgui.ClickGUI
import god.allah.api.event.Event
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import org.lwjgl.input.Keyboard

@Module.Info("ClickGUI", Category.GUI, defaultKey = Keyboard.KEY_RSHIFT)
class ClickGUI : Module() {


    override fun onEvent(event: Event) {
    }

    override fun onEnable() {
        mc.displayGuiScreen(Wrapper.clickGUI)
        setToggled(false)
    }

    override fun onDisable() {

    }

}
