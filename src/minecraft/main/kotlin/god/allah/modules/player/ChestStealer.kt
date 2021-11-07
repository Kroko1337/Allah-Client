package god.allah.modules.player

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.api.utils.TimeHelper
import god.allah.events.GuiHandleEvent
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemAir
import java.lang.NullPointerException


@ModuleInfo("ChestStealer", Category.PLAYER)
class ChestStealer : Module() {

    private val timeHelper = TimeHelper()

    @EventInfo(priority = EventPriority.LOW)
    override fun onEvent(event: Event) {
        when (event) {
            is GuiHandleEvent -> {
                try {
                    if (player.openContainer != null && player.openContainer is ContainerChest) {
                        val container = player.openContainer as ContainerChest
                        for (i in 0 until container.lowerChestInventory.sizeInventory) {
                            mc.inGameHasFocus = true
                            mc.mouseHelper.grabMouseCursor()
                            if (container.lowerChestInventory.getStackInSlot(i).item !is ItemAir && timeHelper.hasReached(
                                    200,
                                    true
                                )
                            ) {
                                playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, player)
                                playerController.windowClick(container.windowId, i, 0, ClickType.PICKUP_ALL, player)
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onEnable() {
    }

    override fun onDisable() {

    }
}