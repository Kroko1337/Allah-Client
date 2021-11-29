package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.api.utils.getDirection
import god.allah.api.utils.setSpeed
import god.allah.events.MoveEvent
import god.allah.events.UpdateEvent
import kotlin.math.sqrt

@Module.Info("Speed", Category.MOVEMENT)
class Speed : Module() {

    @Value("Mode")
    val mode = ComboBox("NCPBhop", arrayOf("NCPBhop", "NCPYPort"))

    override fun getInfo(): String {
        return mode.value
    }

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (mode.value) {
            "NCPYPort" -> {
                when (event) {
                    is MoveEvent -> {
                        if (event.type == MoveEvent.Type.JUMP)
                            event.yaw = getDirection(player.rotationYaw)
                    }
                    is UpdateEvent -> {
                        player.isSprinting = true
                        if (player.onGround) {
                            player.jump()
                            player.motionX *= 0.75
                            player.motionZ *= 0.75
                            timer.timerSpeed = 0.8F
                        } else {
                            if (player.motionY < 0.4) {
                                timer.timerSpeed = 1.6F
                                player.motionY = -1337.0
                                setSpeed(0.26)
                            }
                        }
                    }
                }
            }
            "NCPBhop" -> {
                when (event) {
                    is UpdateEvent -> {
                        if (!mc.gameSettings.keyBindJump.pressed) {
                            timer.timerSpeed = 1F
                            setSpeed(sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) * 1.00455)
                            if (player.onGround) {
                                player.jump()
                            } else {
                                timer.timerSpeed = 1.061f
                                player.motionX *= 1.004F
                                player.motionZ *= 1.004F
                                player.moveStrafing *= 2
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onEnable() {
    }

    override fun onDisable() {
        timer.timerSpeed = 1F
    }

}