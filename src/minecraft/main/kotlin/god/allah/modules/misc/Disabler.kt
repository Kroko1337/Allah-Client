package god.allah.modules.misc

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.events.PacketEvent
import god.allah.events.UpdateEvent
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketKeepAlive
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.server.SPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketEntityTeleport

@Module.Info("Disabler", Category.MISC)
class Disabler : Module() {

    @Value("Mode")
    val mode = ComboBox("BlocksMC", arrayOf("BlocksMC"))

    private var blocksMCTeleport = false

    @EventInfo
    override fun onEvent(event: Event) {
        when (mode.value) {
            "BlocksMC" -> { //TODO: Test it
                when (event) {
                    is PacketEvent -> {
                        when (event.packet) {
                            is CPacketPlayer -> {
                                if (player.ticksExisted % 70 == 0) {
                                    sendMessage("Spoof")
                                    sendPacket(CPacketPlayer.Position(x, y - 13, z, false))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onEnable() {
        blocksMCTeleport = false
    }

    override fun onDisable() {

    }
}