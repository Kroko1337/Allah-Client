package god.allah.modules.gui

import god.allah.api.Registry
import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.Render2DEvent
import org.lwjgl.input.Keyboard


@ModuleInfo("HUD", Category.GUI)
class HUD : Module() {

    init {
        setToggled(true)
    }

    @EventInfo
    override fun onEvent(event: Event) {
        when (event) {
            is Render2DEvent -> {
                var yAxis = 3
                Registry.getEntries(Module::class.java).filter { module -> module.category != Category.GUI }.sortedWith(Comparator { m1: Module, m2: Module -> fr.getStringWidth(m2.name) - fr.getStringWidth(m1.name) } as Comparator<Module>).forEach { module ->
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