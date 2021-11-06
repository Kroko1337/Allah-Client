package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.ComboBox
import god.allah.events.PacketEvent
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion

@ModuleInfo("Velocity", Category.COMBAT)
class Velocity : Module() {

    @Value("Mode")
    var mode = ComboBox("Cancel", modes = arrayOf("Cancel"))

    @Value("Cancel Explosion")
    var cancelExplosion = CheckBox(true)

    @EventInfo
    override fun onEvent(event: Event) {
        when (mode.value) {
            "Cancel" -> {
                when (event) {
                    is PacketEvent -> {
                        when (event.packet) {
                            is SPacketEntityVelocity -> {
                                event.canceled = true
                            }
                            is SPacketExplosion -> {
                                if (cancelExplosion.value)
                                    event.canceled = true
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
    }
}