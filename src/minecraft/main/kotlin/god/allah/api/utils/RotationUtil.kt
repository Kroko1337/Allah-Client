package god.allah.api.utils

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import god.allah.api.Wrapper.mc
import god.allah.api.Wrapper
import god.allah.api.helper.PlayerHandler
import god.allah.events.MouseOverEvent
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

fun getRotation(player: EntityPlayer = Wrapper.player, blockPos: BlockPos, mouseSensitivity: Boolean, prediction: Boolean = true) : Array<Float> {
    val mc = Minecraft.getMinecraft()
    val block = mc.world.getBlockState(blockPos)
    val bounding = block.getBoundingBox(mc.world,blockPos)

    val x = (blockPos.x + 0.5) - player.posX - (if(prediction) player.motionX else 0.0)
    val y = (blockPos.y - (bounding.maxY - bounding.minY + 0.5)) - (player.posY + player.eyeHeight)
    val z = (blockPos.z + 0.5) - player.posZ - (if(prediction) player.motionZ else 0.0)

    val angle = MathHelper.sqrt(x * x + z * z).toDouble()
    val yawAngle = ((MathHelper.atan2(z, x) * 180.0 / PI).toFloat() - 90.0f)
    val pitchAngle = -(MathHelper.atan2(y, angle) * 180.0 / Math.PI).toFloat()
    val yaw = updateRotation(PlayerHandler.currentYaw, yawAngle, 180.0F)
    val pitch = updateRotation(PlayerHandler.currentPitch, pitchAngle, 180.0F)
    return handleMouseSensitivity(mouseSensitivity, yaw, pitch, PlayerHandler.currentYaw, PlayerHandler.currentPitch)
}


fun getRotation(
    player: Entity,
    target: Entity,
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
    val pitch = updateRotation(PlayerHandler.currentPitch, pitchAngle, 180.0F)
    val yaw = updateRotation(PlayerHandler.currentYaw, yawAngle, 180.0F)
    return handleMouseSensitivity(mouseSensitivity, yaw, pitch, PlayerHandler.currentYaw, PlayerHandler.currentPitch)
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

private var mouseOver: RayTraceResult? = null
private var pointedEntity: Entity? = null

fun rayCast(range: Double, yaw: Float = PlayerHandler.yaw, pitch: Float = PlayerHandler.pitch) : RayTraceResult? {
    val entity: Entity? = mc.renderViewEntity
    if (entity != null && mc.world != null) {
        mc.pointedEntity = null
        var d0: Double = range
        mouseOver = entity.rayTrace(d0, 1F)
        val vec3d = entity.getPositionEyes(1F)
        var flag = false
        val i = 3
        var d1 = d0
        if (mc.playerController.extendedReach()) {
            d1 = 6.0
            d0 = d1
        } else if (d0 > range) {
            flag = true
        }
        if (mc.objectMouseOver != null) {
            d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d)
        }
        val vec3d1 = entity.getLook(1.0f)
        val vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0)
        pointedEntity = null
        var vec3d3: Vec3d? = null
        val f = 1.0f
        val list: List<Entity> = mc.world.getEntitiesInAABBexcluding(
            entity,
            entity.entityBoundingBox.expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0, 1.0, 1.0),
            Predicates.and(EntitySelectors.NOT_SPECTATING,
                Predicate { p_apply_1_ -> p_apply_1_ != null && p_apply_1_.canBeCollidedWith() })
        )
        var d2 = d1
        for (j in list.indices) {
            val entity1 = list[j]
            val axisalignedbb = entity1.entityBoundingBox.grow(entity1.collisionBorderSize.toDouble())
            val raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2)
            if (axisalignedbb.contains(vec3d)) {
                if (d2 >= 0.0) {
                    pointedEntity = entity1
                    vec3d3 = if (raytraceresult == null) vec3d else raytraceresult.hitVec
                    d2 = 0.0
                }
            } else if (raytraceresult != null) {
                val d3 = vec3d.distanceTo(raytraceresult.hitVec)
                if (d3 < d2 || d2 == 0.0) {
                    var flag1 = false
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract)
                    }
                    if (!flag1 && entity1.lowestRidingEntity === entity.lowestRidingEntity) {
                        if (d2 == 0.0) {
                            pointedEntity = entity1
                            vec3d3 = raytraceresult.hitVec
                        }
                    } else {
                        pointedEntity = entity1
                        vec3d3 = raytraceresult.hitVec
                        d2 = d3
                    }
                }
            }
        }
        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0) {
            pointedEntity = null
            mouseOver =
                RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null as EnumFacing?, BlockPos(vec3d3))
        }
        if (pointedEntity != null && (d2 < d1 || mouseOver == null)) {
            mouseOver = RayTraceResult(pointedEntity, vec3d3)
            if (pointedEntity is EntityLivingBase || pointedEntity is EntityItemFrame) {
                mc.pointedEntity = pointedEntity
            }
        }
    }
    return mouseOver
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