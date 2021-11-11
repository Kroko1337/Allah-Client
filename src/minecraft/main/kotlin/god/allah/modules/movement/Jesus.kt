package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.api.utils.isMoving
import god.allah.api.utils.random
import god.allah.api.utils.setSpeed
import god.allah.events.UpdateEvent

@Module.Info("Jesus", Category.MOVEMENT)
class Jesus : Module() {

    @Value("Mode")
    var mode = ComboBox("Intave13", arrayOf("Intave13", "Karhu 2.2 169"))

    override fun getInfo(): String {
        return mode.value
    }

    @EventInfo
    override fun onEvent(event: Event) {
        when (mode.value) {
            "Intave13" -> {
                when (event) {
                    is UpdateEvent -> {
                        if (player.isInWater && !player.collidedHorizontally&& !player.collidedHorizontally) {
                            player.motionY = 0.02
                            if (isMoving())
                                setSpeed(0.145)
                        }
                    }
                }
            }
            "Karhu 2.2 169" -> {
                when(event) {
                    is UpdateEvent -> {
                        if(player.isInWater && !player.collidedHorizontally) {
                            player.motionY = 0.005
                            if(isMoving())
                                setSpeed(0.45)
                        }
                    }
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {
        timer.setTimerSpeed(1.0F)
    }
}