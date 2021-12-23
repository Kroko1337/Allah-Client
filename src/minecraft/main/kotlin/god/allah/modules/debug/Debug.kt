package god.allah.modules.debug

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.utils.setSpeed
import god.allah.events.SyncItemEvent
import god.allah.events.UpdateEvent
import net.minecraft.network.play.client.CPacketPlayer

@Module.Info("Debug", Category.DEBUG)
class Debug : Module() {

    var count = 0.0

    @EventInfo(EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is SyncItemEvent -> {
                event.canceled = true
            }
           /* is UpdateEvent -> {
                if (player.hurtTime != 0 && player.collidedHorizontally)
                    setSpeed(0.2)
            }*/
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
    }

    override fun onEnable() {
        /*if (!player.foodStats.needFood()) {
            count = 0.0
            run {
                var distance = 0.06
                while (distance < 3.075) {
                    sendPacket(CPacketPlayer.Position(x, y + 0.06, z, false))
                    distance += 0.1E-8
                    sendPacket(CPacketPlayer.Position(x, y + 0.1E-8, z, false))
                    distance += 0.06
                }
            }
            sendPacket(CPacketPlayer.Position(x, y, z, false))
            sendPacket(CPacketPlayer(true))
        } else {
            sendMessage("§cYou are hungry please eat some food!", actionBar = true, prefix = false)
            setToggled(false)
        }*/
    }

    override fun onDisable() {
        timer.timerSpeed = 1F
    }
}