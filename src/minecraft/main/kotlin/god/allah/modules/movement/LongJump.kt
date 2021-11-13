package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.api.utils.setSpeed
import god.allah.events.UpdateEvent
import net.minecraft.network.play.client.CPacketPlayer

@Module.Info("LongJump", Category.MOVEMENT)
class LongJump : Module() {

    @Value("Mode")
    val mode = ComboBox("Sentinel", arrayOf("Sentinel"))

    var sentinelState = 0
    var sentinelWasAir = false

    override fun onEvent(event: Event) {
        when(mode.value) {
            "Sentinel" -> {
                when(event) {
                    is UpdateEvent -> {
                        if (player.hurtTime != 0) {
                            timer.timerSpeed = 0.4F
                            player.isSprinting = true
                            when (sentinelState) {
                                0 -> {
                                    timer.timerSpeed = 1F
                                    player.jump()
                                }
                                1 -> {
                                    sentinelWasAir = true
                                    for (i in 0..2)
                                        player.jump()
                                }
                                2 -> {
                                    player.motionY = 0.42
                                }
                                else -> {
                                    setSpeed(0.4)
                                    player.motionY += 0.05
                                }
                            }
                            sentinelState++
                        } else if (player.onGround) {
                            if(sentinelWasAir)
                                setToggled(false)
                            timer.timerSpeed = 1F
                            sentinelState = 0
                        }
                    }
                }
            }
        }
    }

    override fun onEnable() {
        sentinelState = 0
        sentinelWasAir = false
        when(mode.value) {
            "Sentinel" -> {
                if (!player.foodStats.needFood()) {
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
                }
            }
        }
    }

    override fun onDisable() {
        timer.timerSpeed = 1F
    }
}