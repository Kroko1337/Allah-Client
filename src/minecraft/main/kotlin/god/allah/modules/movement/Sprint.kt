package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo

@ModuleInfo("Sprint", Category.MOVEMENT)
class Sprint : Module() {

    @EventInfo(EventPriority.LOW)
    override fun onEvent(event: Event) {

    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}