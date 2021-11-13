package god.allah.api.utils

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import god.allah.api.Wrapper
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.src.Reflector
import net.minecraft.util.EntitySelectors
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.*
import kotlin.math.PI


fun getBestVector(look: Vec3d, axisAlignedBB: AxisAlignedBB): Vec3d {
    return Vec3d(
        MathHelper.clamp(look.x, axisAlignedBB.minX, axisAlignedBB.maxX),
        MathHelper.clamp(look.y, axisAlignedBB.minY, axisAlignedBB.maxY),
        MathHelper.clamp(look.z, axisAlignedBB.minZ, axisAlignedBB.maxZ)
    )
}

fun getRotation(player: EntityPlayer = Wrapper.player, blockPos: BlockPos, prevYaw: Float, prevPitch: Float, mouseSensitivity: Boolean, prediction: Boolean = true) : Array<Float> {
    val mc = Minecraft.getMinecraft()
    val block = mc.world.getBlockState(blockPos)
    val bounding = block.getBoundingBox(mc.world,blockPos)

    val x = (blockPos.x + 0.5) - player.posX - (if(prediction) player.motionX else 0.0)
    val y = (blockPos.y - (bounding.maxY - bounding.minY + 0.5)) - (player.posY + player.eyeHeight)
    val z = (blockPos.z + 0.5) - player.posZ - (if(prediction) player.motionZ else 0.0)

    val angle = MathHelper.sqrt(x * x + z * z).toDouble()
    val yawAngle = ((MathHelper.atan2(z, x) * 180.0 / PI).toFloat() - 90.0f)
    val pitchAngle = -(MathHelper.atan2(y, angle) * 180.0 / Math.PI).toFloat()
    val yaw = updateRotation(prevYaw, yawAngle, 180.0F)
    val pitch = updateRotation(prevPitch, pitchAngle, 180.0F)
    return handleMouseSensitivity(mouseSensitivity, yaw, pitch, prevYaw, prevPitch)
}


fun getRotation(
    player: Entity,
    target: Entity,
    prevYaw: Float,
    prevPitch: Float,
    bestVector: Boolean,
    mouseSensitivity: Boolean = true,
    heuristics: Boolean = true
): Array<Float> {
    val eyeX = player.posX
    val eyeY = player.posY + player.eyeHeight
    val eyeZ = player.posZ

    var x = target.posX - eyeX
    var y = target.posY + target.eyeHeight - eyeY
    var z = target.posZ - eyeZ

    if (bestVector) {
        val bestVec = getBestVector(player.getPositionEyes(Wrapper.mc.timer.renderPartialTicks), target.entityBoundingBox)
        x = bestVec.x - eyeX
        y = bestVec.y - eyeY
        z = bestVec.z - eyeZ
    }

    if (target !is EntityLivingBase)
        y = (target.entityBoundingBox.minY + target.entityBoundingBox.maxY) / 2.0

    if (heuristics) {
        x += MathHelper.clamp(player.motionX * -1 * random(0.1, 1.0), -0.03, 0.03)
        y += random(-0.05, 0.05)
        z += MathHelper.clamp(player.motionZ * -1 * random(0.1, 1.0), -0.03, 0.03)
    }

    val sprinting: Boolean = target.isSprinting
    val sprintingPlayer: Boolean = player.isSprinting

    val walkingSpeed = 0.10000000149011612f //https://minecraft.fandom.com/wiki/Sprinting

    val sprintMultiplication = if (sprinting) 1.25f else walkingSpeed
    val sprintMultiplicationPlayer = if (sprintingPlayer) 1.25f else walkingSpeed

    val xMultiplication: Float = (((target.posX - target.prevPosX) * sprintMultiplication).toFloat())
    val zMultiplication: Float = (((target.posZ - target.prevPosZ) * sprintMultiplication).toFloat())

    val xMultiplicationPlayer = (((player.posX - player.prevPosX) * sprintMultiplicationPlayer).toFloat())
    val zMultiplicationPlayer = (((player.posZ - player.prevPosZ) * sprintMultiplicationPlayer).toFloat())


    if (xMultiplication != 0.0f && zMultiplication != 0.0f || xMultiplicationPlayer != 0.0f && zMultiplicationPlayer != 0.0f) {
        x += xMultiplication + xMultiplicationPlayer
        z += zMultiplication + zMultiplicationPlayer
    }


    val angle = MathHelper.sqrt(x * x + z * z).toDouble()
    val yawAngle = (MathHelper.atan2(z, x) * (180.0 / Math.PI)).toFloat() - 90.0f
    val pitchAngle = (-(MathHelper.atan2(y, angle) * (180.0 / Math.PI))).toFloat()
    val pitch = updateRotation(prevPitch, pitchAngle, 180.0F)
    val yaw = updateRotation(prevYaw, yawAngle, 180.0F)
    return handleMouseSensitivity(mouseSensitivity, yaw, pitch, prevYaw, prevPitch)
}

fun handleMouseSensitivity(mouseSensitivity: Boolean, yaw: Float, pitch: Float, prevYaw: Float, prevPitch: Float) : Array<Float> {
    if(!mouseSensitivity)
        return arrayOf(yaw, correctPitch(pitch))
    val sensitivity = 0.5f
    val f = sensitivity * 0.6f + 0.2f
    val f1 = f * f * f * 8.0f
    val deltaYaw = yaw - prevYaw
    val deltaPitch: Float = pitch - prevPitch

    val f2: Float = (deltaYaw * f1 * 7).toInt().toFloat()
    val f3: Float = (deltaPitch * f1 * 7).toInt().toFloat()

    val angles = setAngles(prevYaw, prevPitch, f2, f3)
    return arrayOf(angles[0], correctPitch(angles[1]))
}

fun correctPitch(pitch: Float) : Float {
    return MathHelper.clamp(pitch, -90.0F, 90.0F)
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

fun rayCastedEntity(range: Double, yaw: Float, pitch: Float, partialTicks: Float): Entity? {
    var range = range
    val entity: Entity? = Wrapper.mc.renderViewEntity
    var pointedEntity: Entity? = null
    if (entity != null && Wrapper.mc.world != null) {
        var mouseOver: RayTraceResult? = entity.rayTrace(range, partialTicks)
        val vec3: Vec3d = entity.getPositionEyes(1f)
        val flag = false
        var d1 = range
        if (mouseOver != null) {
            d1 = mouseOver.hitVec.distanceTo(vec3)
        }
        val vec31: Vec3d = getLook(Wrapper.mc.player, yaw, pitch, partialTicks)
        val vec32: Vec3d = vec3.add(vec31.x * range, vec31.y * range, vec31.z * range)
        pointedEntity = null
        var vec33: Vec3d? = null
        val f = 1.0f
        val list: List<*> = Wrapper.mc.world.getEntitiesInAABBexcluding(
            entity,
            entity.entityBoundingBox.expand(vec31.x * range, vec31.y * range, vec31.z * range).expand(
                f.toDouble(), f.toDouble(), f.toDouble()
            ),
            Predicates.and(EntitySelectors.NOT_SPECTATING,
                Predicate { obj -> obj?.canBeCollidedWith() ?: false })
        )
        var d2 = d1
        for (i in list.indices) {
            val entity1 = list[i] as Entity
            val f1 = entity1.collisionBorderSize
            val axisalignedbb = entity1.entityBoundingBox.expand(
                f1.toDouble(), f1.toDouble(),
                f1.toDouble()
            )
            val movingobjectposition: RayTraceResult? = axisalignedbb.calculateIntercept(vec3, vec32)
            if (axisalignedbb.contains(vec3)) {
                if (range >= 0.0) {
                    pointedEntity = entity1
                    vec33 = if (movingobjectposition == null) vec3 else movingobjectposition.hitVec
                    range = 0.0
                }
            } else if (movingobjectposition != null) {
                val d3: Double = vec3.distanceTo(movingobjectposition.hitVec)
                if (d3 < range || range == 0.0) {
                    var flag2 = false
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, *arrayOfNulls(0))
                    }
                    if (entity1 === entity.ridingEntity && !flag2) {
                        if (d2 == 0.0) {
                            pointedEntity = entity1
                            vec33 = movingobjectposition.hitVec
                        }
                    } else {
                        pointedEntity = entity1
                        vec33 = movingobjectposition.hitVec
                        d2 = d3
                    }
                }
            }
        }
        if (pointedEntity != null && flag && vec3.distanceTo(vec33) > range) {
            pointedEntity = null
            mouseOver = RayTraceResult(
                RayTraceResult.Type.MISS,
                vec33,
                null as EnumFacing?,
                BlockPos(vec33)
            )
        }
        if (pointedEntity != null && (d2 < d1 || Wrapper.mc.objectMouseOver == null)) {
            mouseOver = RayTraceResult(pointedEntity, vec33)
            if (pointedEntity is EntityLivingBase || pointedEntity is EntityItemFrame) {
                return pointedEntity
            }
        }
        if (mouseOver != null && mouseOver.entityHit != null) return mouseOver.entityHit
    }
    return pointedEntity
}


fun getLook(
    entity: Entity,
    yaw: Float,
    pitch: Float,
    partialTicks: Float = 1F,
    prevPitch: Float = 0.0F,
    prevYaw: Float = 0.0F
): Vec3d {
    return if (partialTicks == 1.0f) {
        entity.getVectorForRotation(pitch, yaw)
    } else {
        val f: Float = (prevPitch + pitch - prevPitch) * partialTicks
        val f1: Float = (prevYaw + yaw - prevYaw) * partialTicks
        entity.getVectorForRotation(f, f1)
    }
}