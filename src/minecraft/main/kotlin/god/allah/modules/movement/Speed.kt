package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.utils.isMoving
import god.allah.events.UpdateEvent
import org.lwjgl.input.Keyboard

@ModuleInfo("Speed", Category.MOVEMENT, defaultKey = Keyboard.KEY_G)
class Speed : Module() {

    @Value("AutoJump")
    val autojump = CheckBox(true)

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                if (!autojump.value && isMoving()) return
                if (ground) {
                    player.jump()
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }

}