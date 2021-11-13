package god.allah.modules.world

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.PlayerHandler
import god.allah.api.helper.TimeHelper
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.SliderSetting
import god.allah.api.utils.getHotBarSlotClassified
import god.allah.api.utils.getRotation
import god.allah.events.AttackEvent
import god.allah.events.RotationEvent
import god.allah.events.SyncItemEvent
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

@Module.Info("Scaffold", Category.WORLD)
class Scaffold : Module() {

    @Value("Delay")
    val delay = SliderSetting(0L, 0L, 500L)

    @Value("Range")
    val range = SliderSetting(1, 1, 5)

    @Value("Silent")
    val silent = CheckBox(true)

    @Value("RayCast")
    val rayCast = CheckBox(true)

    @Value("Mouse Sensitivity")
    val mouseSensitivity = CheckBox(true)

    private val delayTime = TimeHelper()
    private var blockPos: BlockPos? = null
    private lateinit var itemStack: ItemStack
    private var yaw = 0.0F
    private var pitch = 0.0F

    @EventInfo
    override fun onEvent(event: Event) {
        when (event) {
            is RotationEvent -> {
                if (blockPos != null) {
                    val rotation = getRotation(player, blockPos!!, yaw, pitch, mouseSensitivity.value)
                    event.yaw = rotation[0]
                    event.pitch = rotation[1]
                    yaw = rotation[0]
                    pitch = rotation[1]
                } else {
                    event.yaw = yaw
                    event.pitch = pitch
                }
            }
            is SyncItemEvent -> {
                if (silent.value)
                    event.canceled = true
            }
            is AttackEvent -> {
                if (!playerController.isHittingBlock) {
                    mc.rightClickDelayTimer = 4
                    if (!player.isRowingBoat) {

                        if (!silent.value && player.getHeldItem(EnumHand.MAIN_HAND).item is ItemBlock) {
                            itemStack = player.getHeldItem(EnumHand.MAIN_HAND)
                        } else
                            if (itemStack.item !is ItemBlock && silent.value && (player.inventory.getCurrentItem().item !is ItemBlock || !PlayerHandler.hasSilent())) {
                                val slot = getHotBarSlotClassified<ItemBlock>()
                                itemStack = player.inventory.getStackInSlot(getHotBarSlotClassified<ItemBlock>())
                                sendPacket(CPacketHeldItemChange(slot))
                            }
                        blockPos = searchBlock(range.value)
                        if (blockPos != null) {
                            if (delayTime.hasReached(delay.value)) {
                                if (world.getBlockState(blockPos!!).material !== Material.AIR) {
                                    val i: Int = itemStack.count
                                    val vecHit = if (rayCast.value) mc.objectMouseOver.hitVec else Vec3d(
                                        blockPos!!.x + 0.5,
                                        blockPos!!.y + 0.5,
                                        blockPos!!.z + 0.5
                                    )
                                    val sideHit =
                                        if (rayCast.value) mc.objectMouseOver.sideHit else EnumFacing.getFacingFromVector(vecHit.x.toFloat(), vecHit.y.toFloat(), vecHit.z.toFloat())
                                    sendMessage(sideHit, actionBar = true)
                                    val action = playerController.processRightClickBlock(
                                        player,
                                        world,
                                        blockPos!!,
                                        sideHit,
                                        vecHit,
                                        EnumHand.MAIN_HAND
                                    )
                                    when (action) {
                                        EnumActionResult.SUCCESS -> {
                                            player.swingArm(EnumHand.MAIN_HAND)
                                            if (!itemStack.isEmpty && (itemStack.count != i || playerController.isInCreativeMode)) {
                                                mc.entityRenderer.itemRenderer.resetEquippedProgress(EnumHand.MAIN_HAND)
                                            }
                                            return
                                        }
                                    }
                                }
                                if (!itemStack.isEmpty && playerController.processRightClick(
                                        player,
                                        world,
                                        EnumHand.MAIN_HAND
                                    ) == EnumActionResult.SUCCESS
                                ) {
                                    mc.entityRenderer.itemRenderer.resetEquippedProgress(EnumHand.MAIN_HAND)
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasBlocksAround(blockPos: BlockPos): Boolean {
        return !world.isAirBlock(blockPos.add(1, 0, 0)) || !world.isAirBlock(
            blockPos.add(
                -1,
                0,
                0
            )
        ) || !world.isAirBlock(blockPos.add(0, 0, 1)) || !world.isAirBlock(blockPos.add(0, 0, -1)) || !world.isAirBlock(
            blockPos.add(0, -1, 0)
        )
    }

    private fun searchBlock(range: Int): BlockPos? {
        val playerPos = player.positionVector.add(0.0, -1.0, 0.0)
        var bestPosition: BlockPos? = null

        for (x in -range until range)
            for (y in -1 until 1)
                for (z in -range until range) {
                    val position = Vec3d(
                        playerPos.x.toInt().toDouble(),
                        playerPos.y.toInt().toDouble(),
                        playerPos.z.toInt().toDouble()
                    ).add(x.toDouble(), y.toDouble(), z.toDouble())
                    if (!world.isAirBlock(BlockPos(playerPos))) {
                        if (bestPosition == null || playerPos.distanceTo(position.add(0.5, -0.5, 0.5))
                            <
                            playerPos.distanceTo(
                                Vec3d(
                                    bestPosition.x + 0.5,
                                    bestPosition.y - 0.5,
                                    bestPosition.z + 0.5
                                )
                            )
                        )
                            bestPosition = BlockPos(position)
                    }
                }
        return bestPosition
    }

    override fun onEnable() {
        delayTime.reset()
        blockPos = null
        yaw = player.rotationYaw
        pitch = player.rotationPitch
        itemStack = player.inventory.getCurrentItem()
    }

    override fun onDisable() {
        if(!PlayerHandler.hasAlready(player.inventory.currentItem))
            sendPacket(CPacketHeldItemChange(player.inventory.currentItem))
    }
}