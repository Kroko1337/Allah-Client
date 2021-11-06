package god.allah.modules.visual

import god.allah.api.Registry
import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.Render2DEvent
import org.lwjgl.input.Keyboard

@ModuleInfo("HUD", Category.VISUAL, defaultKey = Keyboard.KEY_V)
class Speed : Module() {


    @EventInfo
    override fun onEvent(event: Event) {
        when (event) {
            is Render2DEvent -> {
                var yAxis = 3
                Registry.getEntries(Module::class.java).forEach { module ->
                    if (module.isToggled()) {
                        fr.drawStringWithShadow(module.name, 4.0f, yAxis.toFloat(), -1)
                        yAxis += fr.FONT_HEIGHT
                    }
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }

}