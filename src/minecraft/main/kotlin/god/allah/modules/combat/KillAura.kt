package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.api.helper.RotationHandler
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.ComboBox
import god.allah.api.setting.types.SliderSetting
import god.allah.api.utils.getRotation
import god.allah.api.utils.rayCastedEntity
import god.allah.events.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.util.EnumHand
import org.lwjgl.input.Keyboard

@ModuleInfo("KillAura", Category.COMBAT, defaultKey = Keyboard.KEY_R)
class KillAura : Module() {

    @Value("Range")
    private val range = SliderSetting(3.0, 1.0, 6.0)

    @Value("Target Mode")
    private val targetMode = ComboBox("Single", arrayOf("Single", "Switch", "Hybrid"))

    @Value("Perfect Hit")
    private val perfectHit = CheckBox(true)

    @Value("MoveFix")
    private val moveFix = CheckBox(true)

    @Value("BestVector")
    private val bestVector = CheckBox(true)

    @Value("MouseSensitivity")
    private val mouseSensitivity = CheckBox(true)

    @Value("RayCast")
    private val rayCast = CheckBox(true)


    var target: Entity? = null
    var yaw: Float = 0F
    var pitch: Float = 0F

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateMotionEvent -> {
                when (event.state) {
                    State.PRE -> {
                        if (target != null && mc.inGameHasFocus) {
                            val rotation =
                                getRotation(player, target!!, yaw, pitch, bestVector.value, mouseSensitivity.value)
                            event.yaw = rotation[0]
                            event.pitch = rotation[1]
                            yaw = rotation[0]
                            pitch = rotation[1]
                        }
                    }
                }
            }
            is UpdateEvent -> {
                if (target != null) {
                    if (!isValid(target))
                        target = null
                }
                when (targetMode.value) {
                    "Single" -> {
                        if (target == null || isValid(target))
                            for (entity in world.loadedEntityList) {
                                if (isValid(entity)) {
                                    target = entity
                                    break
                                }
                            }
                    }
                }
            }
            is AttackEvent -> {
                if (target != null) {
                    var f = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).attributeValue
                    val f2 = player.getCooledAttackStrength(0.5f)
                    f *= (0.2f + f2 * f2 * 0.8f)

                    val flag = f2 > 0.9f
                    var flag2 =
                        flag && player.fallDistance > 0.0f && !player.onGround && !player.isOnLadder && !player.isInWater && !player.isPotionActive(
                            MobEffects.BLINDNESS
                        ) && !player.isRiding && target is EntityLivingBase
                    flag2 = flag2 && !player.isSprinting

                    if (flag2) {
                        f *= 1.5f
                    }

                    if (rayCast.value) {
                        target = rayCastedEntity(range.value, yaw, pitch, 1F)
                    }

                    if (isValid(target))
                        if (!perfectHit.value || f == player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).attributeValue) {
                            attackEntity(target!!)
                        }
                }
            }
            is JumpEvent -> {
                if (moveFix.value)
                    event.yaw = RotationHandler.yaw
            }
            is MoveRelativeEvent -> {
                if (moveFix.value)
                    event.yaw = RotationHandler.yaw
            }
        }
    }

    private fun attackEntity(target: Entity) {
        if (mc.leftClickCounter <= 0) {
            if (!player.isRowingBoat) {
                playerController.attackEntity(player, target)
                player.swingArm(EnumHand.MAIN_HAND)
            }
        }
    }

    private fun isValid(entity: Entity?): Boolean {
        if (entity == null) return false
        if (entity == player) return false
        if (entity !is EntityLivingBase) return false
        if (entity.getDistance(player) > (if(rayCast.value) range.value + 1 else range.value)) return false
        if (entity.isDead && entity.deathTime != 0) return false
        return true
    }

    override fun onEnable() {
        yaw = player.rotationYaw
        pitch = player.rotationPitch
        target = null
    }

    override fun onDisable() {

    }


}