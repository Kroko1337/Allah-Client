package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.ComboBox
import god.allah.api.setting.types.SettingCategory
import god.allah.api.setting.types.SliderSetting
import god.allah.events.UpdateEvent
import org.lwjgl.input.Keyboard

@Module.Info("Sprint", Category.MOVEMENT)
class Sprint : Module() {

    @Value("Legit")
    var legit = CheckBox(true)

    @EventInfo(priority = EventPriority.LOW)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                if (legit.value)
                    mc.gameSettings.keyBindSprint.pressed = true
                else
                    player.isSprinting = true
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}