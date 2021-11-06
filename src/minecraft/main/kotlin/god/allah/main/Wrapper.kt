package god.allah.main

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.network.Packet

object Wrapper {
    const val name = "Allah"
    const val version = "v1.0"
    lateinit var instance: Main

    const val prefix = "§c$name §7>> §f"

    val mc: Minecraft = Minecraft.getMinecraft()
    val player: EntityPlayerSP = mc.player

    fun sendPacket(packet: Packet<*>) {
        mc.connection?.sendPacket(packet)
    }
}