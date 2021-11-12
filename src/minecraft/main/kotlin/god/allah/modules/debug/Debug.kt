package god.allah.modules.debug

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.events.NoClipEvent
import god.allah.events.UpdateEvent

@Module.Info("Debug", Category.DEBUG)
class Debug : Module() {

    @EventInfo(EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                if(player.isInWater)
                {
                    player.onGround = true
                    player.motionY = 0.0
                }
            }
        }
            /*is NoClipEvent -> {
                if (player.isInWater)
                    event.noClip = true
            }
            is UpdateEvent -> {
                if (player.isInWater) {
                    stopMove()
                    player.onGround = true
                    player.isSprinting = true
                    if(player.ticksExisted % 5 == 0) {
                        player.jump()
                    }
                    player.motionY = -1E-3
                } else {
                    resumeMove()
                }
                sendMessage(player.motionY, actionBar = true)
            }
        }*/
    }

    override fun onEnable() {
    }

    override fun onDisable() {

    }
}