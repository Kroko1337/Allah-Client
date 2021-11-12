package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.TimeHelper
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.api.setting.types.SliderSetting
import god.allah.events.SlowdownEvent
import god.allah.events.UpdateEvent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.network.play.client.CPacketHeldItemChange
import kotlin.math.roundToLong
import kotlin.random.Random

@Module.Info("NoSlowDown", Category.MOVEMENT)
class NoSlowDown : Module() {

    @Value("Slowdown")
    var slowdown = SliderSetting(0.2F, 0.1F, 1F)

    @Value("Spoof Mode")
    var spoofMode = ComboBox("None", arrayOf("None", "Intave13", "AAC4"))

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is SlowdownEvent -> {
                event.slowdown = slowdown.value
                when(spoofMode.value) {
                    "Intave13" -> {
                        val nextItem = if(player.inventory.currentItem == 0) 1 else -1
                        sendPacket(CPacketHeldItemChange(player.inventory.currentItem + nextItem))
                        sendPacket(CPacketHeldItemChange(player.inventory.currentItem))
                    }
                    "AAC4" -> {
                        sendPacket(CPacketHeldItemChange(player.inventory.currentItem))
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