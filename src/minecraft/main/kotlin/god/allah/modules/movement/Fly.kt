package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.UpdateEvent

@ModuleInfo("Fly", Category.MOVEMENT)
class Fly : Module() {

    @EventInfo(priority = EventPriority.NORMAL)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                player.capabilities.isFlying = true
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {
        player.capabilities.isFlying = false
    }

}