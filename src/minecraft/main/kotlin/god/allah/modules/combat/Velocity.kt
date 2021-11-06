package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo

@ModuleInfo("Velocity", Category.COMBAT)
class Velocity : Module() {
    @EventInfo(EventPriority.HIGH)
    override fun onEvent(event: Event) {
    }

    override fun onEnable() {
    }

    override fun onDisable() {
    }
}