package god.allah.api.utils

import god.allah.main.Wrapper.mc
import god.allah.main.Wrapper.player
import god.allah.main.Wrapper.sendPacket
import net.minecraft.entity.Entity
import net.minecraft.network.play.client.CPacketPlayer
import org.lwjgl.input.Keyboard
import kotlin.math.cos
import kotlin.math.sin

fun getDirection(rotationYaw: Float, repeat: Boolean = false): Float {
    if (repeat)
        Keyboard.enableRepeatEvents(true)
    val left =
        if (mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindLeft.pressed) -45.0F else if (mc.gameSettings.keyBindBack.pressed && mc.gameSettings.keyBindLeft.pressed) 45.0F else if (mc.gameSettings.keyBindLeft.pressed) -90.0F else 0.0F
    val right =
        if (mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindRight.pressed) 45.0F else if (mc.gameSettings.keyBindBack.pressed && mc.gameSettings.keyBindRight.pressed) -45.0F else if (mc.gameSettings.keyBindRight.pressed) 90.0F else 0.0F
    val back = if (mc.gameSettings.keyBindBack.pressed) 180.0F else 0.0F
    return rotationYaw + left + right + back
}

fun setSpeed(speed: Double, yaw: Float = player.rotationYaw, direction: Boolean = true, repeat: Boolean = false, onlyWhenPress: Boolean = true) {
    if (keyboardPressed() || !onlyWhenPress) {
        player.motionX = -sin(Math.toRadians((if (direction) getDirection(yaw, repeat) else yaw).toDouble())) * speed
        player.motionZ = cos(Math.toRadians((if (direction) getDirection(yaw, repeat) else yaw).toDouble())) * speed
    }
}

fun blinkTo(
    speed: Double,
    y: Double = player.posY,
    yaw: Float = player.rotationYaw,
    ground: Boolean = player.onGround,
    repeat: Boolean = false,
    onlyWhenPress: Boolean = true) {
    if (keyboardPressed() || !onlyWhenPress) {
        val motionX = -sin(Math.toRadians(getDirection(yaw, repeat).toDouble())) * speed
        val motionZ = cos(Math.toRadians(getDirection(yaw, repeat).toDouble())) * speed
        if (repeat)
            Keyboard.enableRepeatEvents(false)
        sendPacket(CPacketPlayer.Position(player.posX + motionX, y, player.posZ + motionZ, ground))
    }
}

fun teleportTo(speed: Double, y: Double = player.posY, yaw: Float = player.rotationYaw, repeat: Boolean = false, onlyWhenPress: Boolean = true) {
    if (keyboardPressed() ||!onlyWhenPress) {
        val motionX = -sin(Math.toRadians(getDirection(yaw, repeat).toDouble())) * speed
        val motionZ = cos(Math.toRadians(getDirection(yaw, repeat).toDouble())) * speed
        if (repeat)
            Keyboard.enableRepeatEvents(false)
        player.setPosition(player.posX + motionX, y, player.posZ + motionZ)
    }
}

fun keyboardPressed() : Boolean {
    return mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed
}

fun isMoving(): Boolean {
    return player.moveForward != 0f || player.moveStrafing != 0f
}

fun isMoving(entity: Entity): Boolean {
    return entity.lastTickPosX != entity.posX || entity.lastTickPosZ != entity.posZ || entity.lastTickPosY != entity.posY
}