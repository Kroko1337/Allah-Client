package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.ComboBox
import god.allah.events.KnockBackModifierEvent
import god.allah.events.PacketEvent
import god.allah.events.UpdateEvent
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion

@Module.Info("Velocity", Category.COMBAT)
class Velocity : Module() {

    @Value("Mode")
    var mode = ComboBox("Cancel", modes = arrayOf("Cancel", "Intave14", "BackToBlock"))

    @Value("Cancel Explosion")
    var cancelExplosion = CheckBox(true)

    override fun getInfo(): String {
        return mode.value
    }

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
            "Intave14" -> {
                when(event) {
                    is KnockBackModifierEvent -> {
                        event.flag = true
                    }
                }
            }
            "BackToBlock" -> {
                when(event) {
                    is UpdateEvent -> {
                        if(player.hurtTime == 6) {
                            player.motionX *= -1
                            player.motionZ *= -1
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