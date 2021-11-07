package god.allah.modules.player

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.UpdateEvent

@ModuleInfo("FastPlace", Category.PLAYER)
class FastPlace : Module() {

    @EventInfo
    override fun onEvent(event: Event) {
        when(event) {
            is UpdateEvent -> {
                mc.rightClickDelayTimer = 0
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}