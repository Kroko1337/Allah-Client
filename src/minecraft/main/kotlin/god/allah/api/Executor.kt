package god.allah.api

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.multiplayer.PlayerControllerMP
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.network.Packet
import net.minecraft.util.Timer
import net.minecraft.util.text.Style
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

interface Executor {
    val mc: Minecraft get() = Wrapper.mc
    val fr: FontRenderer get() = mc.fontRenderer

    val world: WorldClient get() = mc.world
    val timer: Timer get() = mc.timer

    val player: EntityPlayerSP get() = mc.player
    val playerController: PlayerControllerMP get() = mc.playerController

    val ground: Boolean get() = player.onGround

    val x: Double get() = player.posX
    val y: Double get() = player.posY
    val z: Double get() = player.posZ

    fun isLiquid(block: Block): Boolean {
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER
    }

    fun getBlock(offset: Double): Block {
        return getBlock(player, offset)
    }

    fun getBlock(player: EntityPlayer, offset: Double): Block {
        return world.getBlockState(player.position.add(0.0, offset, 0.0)).block
    }

    fun sendPacket(packet: Packet<*>) {
        Wrapper.sendPacket(packet)
    }

    fun resetRotation(yaw: Float, pitch: Float, silent: Boolean) {
        if (yaw == player.rotationYaw && pitch == player.rotationPitch) return
        if (silent)
            player.rotationYaw = yaw - yaw % 360 + player.rotationYaw % 360
        else {
            player.rotationYaw = yaw
            player.rotationPitch = pitch
        }
    }

    fun createCopy(player: EntityPlayer): EntityOtherPlayerMP {
        val copy = EntityOtherPlayerMP(world, player.gameProfile)
        copy.copyLocationAndAnglesFrom(player)
        copy.inventory = player.inventory
        copy.ridingEntity = player.ridingEntity
        copy.health = player.health
        copy.isSprinting = player.isSprinting
        copy.isSneaking = player.isSneaking
        return copy
    }

    fun clearChat(clearSend: Boolean = false) {
        mc.ingameGUI.getChatGUI().clearChatMessages(clearSend)
    }

    fun sendMessage(message: Any, actionBar: Boolean = false, style: Style? = null, prefix: Boolean = true) {
        Wrapper.sendMessage(message, actionBar, style, prefix)
    }

    fun stopMove() {
        mc.gameSettings.keyBindForward.pressed = false
        mc.gameSettings.keyBindBack.pressed = false
        mc.gameSettings.keyBindLeft.pressed = false
        mc.gameSettings.keyBindRight.pressed = false
    }

    fun resumeMove() {
        if(mc.inGameHasFocus || mc.currentScreen != null && mc.currentScreen!!.allowUserInput) {
            checkPressed(mc.gameSettings.keyBindForward)
            checkPressed(mc.gameSettings.keyBindBack)
            checkPressed(mc.gameSettings.keyBindLeft)
            checkPressed(mc.gameSettings.keyBindRight)
        }
    }

    fun checkPressed(keyBinding: KeyBinding) {
        keyBinding.pressed = isPressed(keyBinding.keyCode)
    }

    fun isPressed(keyCode: Int) : Boolean {
        return if(keyCode < 0) {
            val i = Mouse.getEventButton()
            i - 100 == keyCode
        } else {
            Keyboard.isKeyDown(keyCode)
        }
    }
}