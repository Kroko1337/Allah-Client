package god.allah.events

import god.allah.api.event.Event
import net.minecraft.network.Packet

class PacketEvent(val packet: Packet<*>) : Event() {
}