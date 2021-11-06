package god.allah.api

import god.allah.main.Wrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.multiplayer.PlayerControllerMP
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.Timer
import net.minecraft.util.text.TextComponentString

interface Executor {
    val mc: Minecraft get() = Minecraft.getMinecraft()
    val fr: FontRenderer get() = mc.fontRenderer

    val world: WorldClient get() = mc.world
    val timer: Timer get() = mc.timer

    val player: EntityPlayerSP get() = mc.player
    val playerController: PlayerControllerMP get() = mc.playerController

    val x: Double get() = player.posX
    val y: Double get() = player.posY
    val z: Double get() = player.posZ

    fun sendMessage(message: Any, actionBar: Boolean = false, prefix: Boolean = true) {
        val text = TextComponentString("${Wrapper.prefix}$message")
        player.sendStatusMessage(text, actionBar)
    }
}