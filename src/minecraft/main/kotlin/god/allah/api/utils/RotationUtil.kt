package god.allah.api.utils

import god.allah.main.Wrapper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.hypot


fun getBestVector(look: Vec3d, axisAlignedBB: AxisAlignedBB): Vec3d {
    return Vec3d(
        MathHelper.clamp(look.x, axisAlignedBB.minX, axisAlignedBB.maxX),
        MathHelper.clamp(look.y, axisAlignedBB.minY, axisAlignedBB.maxY),
        MathHelper.clamp(look.z, axisAlignedBB.minZ, axisAlignedBB.maxZ)
    )
}

fun getRotation(
    player: Entity,
    target: Entity,
    prevYaw: Float,
    prevPitch: Float,
    bestVector: Boolean,
    mouseSensi: Boolean = true
): Array<Float> {
    val eyeX = player.posX
    val eyeY = player.posY + player.eyeHeight
    val eyeZ = player.posZ

    var x = target.posX - eyeX
    var y = target.posY + target.eyeHeight - eyeY
    var z = target.posZ - eyeZ

    if (bestVector) {
        val bestVec =
            getBestVector(player.getPositionEyes(Wrapper.mc.timer.renderPartialTicks), target.entityBoundingBox);
        x = bestVec.x - eyeX;
        y = bestVec.y - eyeY;
        z = bestVec.z - eyeZ;
    }

    if (target !is EntityLivingBase)
        y = (target.entityBoundingBox.minY + target.entityBoundingBox.maxY) / 2.0

    val angle = MathHelper.sqrt(x * x + z * z).toDouble()
    val yawAngle = (MathHelper.atan2(z, x) * (180.0 / Math.PI)).toFloat() - 90.0f
    val pitchAngle = (-(MathHelper.atan2(y, angle) * (180.0 / Math.PI))).toFloat()
    var pitch = updateRotation(prevPitch, pitchAngle, 180.0F)
    val yaw = updateRotation(prevYaw, yawAngle, 180.0F)
    pitch = MathHelper.clamp(pitch, -90.0F, 90.0F)
    if (!mouseSensi)
        return arrayOf(yaw, pitch)
    val rotationDelta = hypot(prevYaw - yawAngle, prevPitch - pitchAngle)

    val sensitivity = 0.5f
    val f = sensitivity * 0.6f + 0.2f
    val f1 = f * f * f * 8.0f
    val deltaYaw: Float = yaw - prevYaw
    val deltaPitch: Float = pitch - prevPitch

    val f2: Float = (deltaYaw * f1 * 3)
    val f3: Float = (deltaPitch * f1 * 3)

    val angles = setAngles(prevYaw, prevPitch, f2, f3)
    return arrayOf(angles[0], MathHelper.clamp(angles[1], -90.0F, 90.0F))
}

fun setAngles(currentYaw: Float, currentPitch: Float, yaw: Float, pitch: Float): Array<Float> {
    var currentYaw = currentYaw
    var currentPitch = currentPitch
    currentYaw = (currentYaw.toDouble() + yaw.toDouble() * 0.15).toFloat()
    currentPitch = (currentPitch.toDouble() + pitch.toDouble() * 0.15).toFloat()
    return arrayOf(currentYaw, currentPitch)
}

private fun updateRotation(p_75652_1_: Float, p_75652_2_: Float, p_75652_3_: Float): Float {
    var f = MathHelper.wrapDegrees(p_75652_2_ - p_75652_1_)
    if (f > p_75652_3_) {
        f = p_75652_3_
    }
    if (f < -p_75652_3_) {
        f = -p_75652_3_
    }
    return p_75652_1_ + f
}