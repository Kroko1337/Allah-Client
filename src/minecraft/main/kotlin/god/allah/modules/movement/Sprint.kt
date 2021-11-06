package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.ComboBox
import god.allah.api.setting.types.SettingCategory
import god.allah.api.setting.types.SliderSetting
import god.allah.events.UpdateEvent
import org.lwjgl.input.Keyboard

@ModuleInfo("Sprint", Category.MOVEMENT)
class Sprint : Module() {

    @Value("Test")
    val test = CheckBox(true)

    @Value("Test2")
    val test2 = ComboBox("Test", arrayOf("Test", "Test2"))

    @Value("Slider")
    val slider = SliderSetting<Long>(1, 1, 10)

    @Value("Category")
    val testCategory = SettingCategory(test, test2, slider)

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