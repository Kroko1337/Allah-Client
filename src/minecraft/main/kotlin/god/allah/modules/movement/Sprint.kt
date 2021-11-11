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

    var current = 0

    @Value("Test")
    var test = CheckBox(true)

    @Value("Test2")
    var test2 = ComboBox("Test", arrayOf("Test", "Test2"))

    @Value("Slider")
    var slider = SliderSetting<Long>(1, 1, 10)

    @Value("Category")
    var testCategory = SettingCategory(test, test2, slider)

    @EventInfo(priority = EventPriority.LOW)
    override fun onEvent(event: Event) {
        when(event) {
            is UpdateEvent -> {
                player.isSprinting = true
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}