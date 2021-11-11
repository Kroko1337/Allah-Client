package god.allah.api

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.multiplayer.PlayerControllerMP
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.network.Packet
import net.minecraft.util.Timer
import net.minecraft.util.text.Style

interface Executor {
    val mc: Minecraft get() = Wrapper.mc
    val fr: FontRenderer get() = mc.fontRenderer

    val world: WorldClient get() = mc.world
    val timer: Timer get() = mc.timer

    val player: EntityPlayerSP get() = mc.player
    val playerController: PlayerControllerMP get() = mc.playerController

    val ground : Boolean get() = player.onGround

    val x: Double get() = player.posX
    val y: Double get() = player.posY
    val z: Double get() = player.posZ

    fun isLiquid(block: Block) : Boolean {
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER
    }

    fun getBlock(offset: Double) : Block {
        return getBlock(player, offset)
    }

    fun getBlock(player: EntityPlayer, offset: Double) : Block {
        return world.getBlockState(player.position.add(0.0, offset, 0.0)).block
    }

    fun sendPacket(packet: Packet<*>) {
        Wrapper.sendPacket(packet)
    }

    fun resetRotation(yaw: Float, pitch: Float, silent: Boolean) {
        if(yaw == player.rotationYaw && pitch == player.rotationPitch) return
        if(silent)
            player.rotationYaw = yaw - yaw % 360 + player.rotationYaw % 360
        else {
            player.rotationYaw = yaw
            player.rotationPitch = pitch
        }
    }

    fun clearChat(clearSend: Boolean = false) {
        mc.ingameGUI.getChatGUI().clearChatMessages(clearSend)
    }

    fun sendMessage(message: Any, actionBar: Boolean = false, style: Style? = null, prefix: Boolean = true) {
        Wrapper.sendMessage(message, actionBar, style, prefix)
    }
}