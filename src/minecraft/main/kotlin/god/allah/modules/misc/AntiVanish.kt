package god.allah.modules.misc

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.events.PacketEvent
import net.minecraft.network.play.server.SPacketPlayerListItem
import net.minecraft.util.text.Style
import net.minecraft.util.text.event.ClickEvent


@Module.Info("AntiVanish", Category.MISC)
class AntiVanish : Module() {

    @EventInfo(EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is PacketEvent -> {
                if (event.packet is SPacketPlayerListItem) {
                    if (event.packet.action === SPacketPlayerListItem.Action.UPDATE_LATENCY)
                        for (addPlayerData in event.packet.players) if (!mc.connection?.playerInfoMap?.containsKey(addPlayerData.profile.id)!!) {
                            val style = Style()
                            style.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://de.namemc.com/profile/" + addPlayerData.profile.id.toString())
                            sendMessage("§aA player is vanished!", style = style)
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