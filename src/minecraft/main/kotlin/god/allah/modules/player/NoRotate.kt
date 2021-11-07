package god.allah.modules.player

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.HandlePosLookEvent

@ModuleInfo("NoRotate", Category.PLAYER)
class NoRotate : Module() {
    @EventInfo
    override fun onEvent(event: Event) {
        when(event) {
            is HandlePosLookEvent -> {
                event.yaw = player.rotationYaw
                event.pitch = player.rotationPitch
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}