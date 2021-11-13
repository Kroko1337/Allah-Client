package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.PlayerHandler
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.ComboBox
import god.allah.api.setting.types.SliderSetting
import god.allah.api.helper.TimeHelper
import god.allah.api.setting.Dependency
import god.allah.api.setting.ISetting
import god.allah.api.setting.types.SettingGroup
import god.allah.api.utils.getRotation
import god.allah.api.utils.randomGaussian
import god.allah.api.utils.rayCastedEntity
import god.allah.events.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult

@Module.Info("KillAura", Category.COMBAT)
class KillAura : Module() {

    @Value("Range")
    private var range = SliderSetting(3.0, 1.0, 6.0)

    @Value("Target Mode")
    private var targetMode = ComboBox("Hybrid", arrayOf("Single", "Switch", "Hybrid"))

    @Value("Hybrid Mode")
    private var hybridMode = ComboBox("Nearest", arrayOf("Nearest", "Health"), Dependency(targetMode, "Hybrid"))

    @Value("Perfect Hit")
    private var perfectHit = CheckBox(true)

    @Value("CPS")
    private var cps = SliderSetting<Long>(12, 1, 20, Dependency(perfectHit, false))

    @Value("OnlyPlayer")
    private var onlyPlayer = CheckBox(true)

    @Value("MoveFix")
    private var moveFix = CheckBox(true)

    @Value("Keep Sprint")
    private var keepSprint = CheckBox(false)

    @Value("Slowdown")
    private var slowdown = SliderSetting(0.6, 0.1, 1.0)

    @Value("BestVector")
    private var bestVector = CheckBox(true)

    @Value("Mouse Sensitivity")
    private var mouseSensitivity = CheckBox(true)

    @Value("RayCast")
    private var rayCast = CheckBox(true)

    @Value("Through Walls")
    private var throughWalls = CheckBox(false)

    @Value("No near rotate")
    private var noNearRotate = CheckBox(true)

    @Value("Heuristics")
    private var heuristics = CheckBox(true)

    @Value("No Timer Attack")
    private var noTimerAttack = CheckBox(true)

    @Value("Reset Rotation")
    private var resetRotation = CheckBox(true)

    @Value("Reset Rotation-Mode", "Mode")
    private var resetRotationMode = ComboBox("Silent", arrayOf("Silent", "Visible"), Dependency(resetRotation, true))

    @Value("Attacking")
    private var attackingGroup = SettingGroup(range, targetMode, hybridMode, perfectHit, cps, onlyPlayer, keepSprint, slowdown, throughWalls, noTimerAttack)

    @Value("Rotation")
    private var rotationGroup = SettingGroup(moveFix, bestVector, rayCast, mouseSensitivity, noNearRotate, heuristics, resetRotation, resetRotationMode)

    var target: Entity? = null
    var yaw: Float = 0F
    var pitch: Float = 0F
    var currentTarget = 0
    private val timeHelper = TimeHelper()

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is RotationEvent -> {
                if (target != null && mc.inGameHasFocus) {
                    if ((mc.objectMouseOver != null && mc.objectMouseOver.entityHit == null) || player.getDistance(
                            target
                        ) > 0.5 || !noNearRotate.value
                    ) {
                        val rotation = getRotation(
                            player,
                            target!!,
                            yaw,
                            pitch,
                            bestVector.value,
                            mouseSensitivity.value,
                            heuristics.value
                        )
                        event.yaw = rotation[0]
                        event.pitch = rotation[1]
                        yaw = rotation[0]
                        pitch = rotation[1]
                    } else {
                        val rotation = getRotation(
                            player,
                            target!!,
                            yaw,
                            pitch,
                            bestVector.value,
                            mouseSensitivity.value,
                            heuristics.value
                        )
                        event.yaw = rotation[0]
                        event.pitch = pitch
                        yaw = rotation[0]
                    }
                } else if (!mc.inGameHasFocus) {
                    event.yaw = yaw
                    event.pitch = pitch
                } else {
                    yaw = player.rotationYaw
                    pitch = player.rotationPitch
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
                    "Switch" -> {
                        val list = world.loadedEntityList.filter { entity -> isValid(entity) }
                        if (currentTarget >= list.size)
                            currentTarget = 0
                        if (list.isNotEmpty())
                            target = list[currentTarget]
                    }
                    "Hybrid" -> {
                        when (hybridMode.value) {
                            "Nearest" -> {
                                target = getNearestEntity()
                            }
                            "Health" -> {
                                target = getLowestEntity()
                            }
                        }
                    }
                }
            }
            is AttackEvent -> {
                if (target != null && !player.isHandActive) {
                    if (rayCast.value) {
                        target = rayCastedEntity(range.value, PlayerHandler.yaw, PlayerHandler.pitch, 1F)
                        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && mc.objectMouseOver.entityHit != null)
                            target = mc.objectMouseOver.entityHit
                    }

                    if (target != null) {
                        if (isReady()) {
                            //sendMessage("§aTargeto founded")
                            /* if (target == null || mc.objectMouseOver.entityHit == null)
                                sendMessage("$target -> ${mc.objectMouseOver.entityHit}")*/
                            attackEntity(target!!)
                        }
                    }
                } else {
                    //sendMessage("§cTargeto no founded")
                }
            }
            is KnockBackModifierEvent -> {
                if (keepSprint.value)
                    event.sprint = true
                event.motion = slowdown.value
            }
            is JumpEvent -> {
                if (moveFix.value)
                    event.yaw = PlayerHandler.yaw
            }
            is MoveRelativeEvent -> {
                if (moveFix.value)
                    event.yaw = PlayerHandler.yaw
            }
            is MouseOverEvent -> {
                event.range = range.value
                event.maxRange = range.value
            }
        }
    }

    private fun isReady(): Boolean {
        if (perfectHit.value) {
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
            return f == player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).attributeValue
        } else {
            return timeHelper.hasReached(1000 / cps.value + randomGaussian(20.0).toLong())
        }
        return false
    }

    private fun attackEntity(target: Entity) {
        if (timer.timerSpeed == 1.0F || !noTimerAttack.value)
            if (mc.leftClickCounter <= 0) {
                //sendMessage("§eAttacko")
                if (!player.isRowingBoat) {
                    playerController.attackEntity(player, target)
                    player.swingArm(EnumHand.MAIN_HAND)
                    currentTarget++
                }
            } else {
                //sendMessage("Left Click momento")
            }
    }

    private fun getNearestEntity(): Entity? {
        var target: Entity? = null
        world.loadedEntityList.filter { entity -> isValid(entity) }.forEach { entity ->
            if (target == null || target!!.getDistance(player) > entity.getDistance(player))
                target = entity
        }
        return target
    }

    private fun getLowestEntity(): Entity? {
        var target: EntityLivingBase? = null
        world.loadedEntityList.filter { entity -> isValid(entity) && entity is EntityLivingBase }.forEach { entity ->
            when (entity) {
                is EntityLivingBase -> {
                    if (target == null || target!!.health > entity.health)
                        target = entity
                }
            }
        }
        return target
    }

    private fun isValid(entity: Entity?): Boolean {
        if (entity == null) return false
        if (entity == player) return false
        if (entity !is EntityLivingBase) return false
        if (entity !is EntityPlayer && onlyPlayer.value) return false
        if (entity.getDistance(player) > (range.value + (if (rayCast.value) 1.5 else 0.0))) return false
        if (entity.deathTime != 0) return false
        if (entity.isDead) return false
        if (entity.isInvisible) return false
        if (!player.canEntityBeSeen(entity) && !throughWalls.value) return false
        return true
    }

    override fun onEnable() {
        yaw = player.rotationYaw
        pitch = player.rotationPitch
        target = null
        currentTarget = 0
    }

    override fun onDisable() {
        if (resetRotation.value)
            resetRotation(PlayerHandler.yaw, PlayerHandler.pitch, resetRotationMode.value == "Silent");
    }


}