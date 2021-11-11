package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.utils.isMoving
import god.allah.api.utils.setSpeed
import god.allah.events.UpdateEvent
import org.lwjgl.input.Keyboard
import kotlin.math.sqrt

@Module.Info("Speed", Category.MOVEMENT)
class Speed : Module() {

    @Value("AutoJump")
    var autoJump = CheckBox(true)

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                if (!Keyboard.isKeyDown(57)) {
                    timer.setTimerSpeed(1F)
                    setSpeed(sqrt(player.motionX * player.motionX + player.motionZ *player.motionZ) * 1.00455)
                    if (player.onGround) {
                        player.jump()
                    } else {
                        timer.setTimerSpeed(1.061f)
                        player.motionX *= 1.004F
                        player.motionZ *= 1.004F
                        player.moveStrafing *= 2
                    }

                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }

}