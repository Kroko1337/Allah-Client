package god.allah.modules.world

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.events.SyncItemEvent

@Module.Info("Scaffold", Category.WORLD)
class Scaffold : Module() {
    @EventInfo
    override fun onEvent(event: Event) {
        when(event) {
            is SyncItemEvent -> {
                event.canceled = true
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}