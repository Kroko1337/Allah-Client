package god.allah.modules.visual

import god.allah.api.event.Event
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.events.Render3DEvent
import god.allah.events.SyncItemEvent

@Module.Info(name = "ESP", category = Category.VISUAL)
class ESP : Module() {

    override fun onEvent(event: Event) {
        when (event) {
            is Render3DEvent -> {

                for (entity in world.loadedEntityList) {
                    if (entity == player) {
                        return
                    }

                    var xAxis : Double = entity.lastTickPosX

                }

            }
        }
    }


    override fun onEnable() {

    }

    override fun onDisable() {
    }
}