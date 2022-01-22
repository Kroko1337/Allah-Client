package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.api.utils.getDirection
import god.allah.api.utils.isMoving
import god.allah.api.utils.setSpeed
import god.allah.events.MoveEvent
import god.allah.events.UpdateEvent
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketPlayerAbilities
import kotlin.math.sqrt

@Module.Info("Speed", Category.MOVEMENT)
class Speed : Module() {

    @Value("Mode")
    val mode = ComboBox("NCPBhop", arrayOf("NCPBhop", "NCPYPort", "Sentinel"))

    override fun getInfo(): String {
        return mode.value
    }

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (mode.value) {
            "Sentinel" -> {
                when (event) {
                    is UpdateEvent -> {
                        var boost = 0.43
                        if (!player.foodStats.needFood()) {
                            boost = 0.27
                        }
                        player.isSprinting = true
                        player.jumpMovementFactor = 0.03F
                        if (isMoving()) {
                            if (player.onGround) {
                                player.jump()
                                setSpeed(boost)
                                timer.timerSpeed = 1.0F
                            } else {
                                if (player.motionY < 0)
                                    timer.timerSpeed = 1.5F + ((player.motionY.toFloat()) * -0.5F)
                                else
                                    timer.timerSpeed = 1F
                                setSpeed(0.27)
                            }
                        }
                    }
                }
            }
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
        player.jumpMovementFactor = 0.02F
    }

}