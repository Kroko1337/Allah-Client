package god.allah.modules.player

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.GuiHandleEvent
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.Container


@ModuleInfo("ChestStealer", Category.PLAYER)
class ChestStealer : Module() {

    @EventInfo(priority = EventPriority.LOW)
    override fun onEvent(event: Event) {
        when(event) {
            is GuiHandleEvent -> {
                val chest = player.openContainer
                for (i in 0 until chest.inventorySlots.size) {
                    val itemStack = chest.inventorySlots[i]
                    mc.inGameHasFocus = true
                    mc.mouseHelper.grabMouseCursor()
                    if (itemStack != null) {
                        if (player.ticksExisted % 5 == 0) {
                            mc.playerController.windowClick(chest.windowId, i, 0,  ClickType.THROW, player)
                        }
                    }
                }
            }
        }
    }

    override fun onEnable() {
    }

    private fun containerEmpty(container: Container): Boolean {
        var empty = true
        var i = 0
        val slot = if (container.inventorySlots.size === 90) 54 else 27
        while (i < slot) {
            if (container.getSlot(i).hasStack) {
                empty = false
            }
            ++i
        }
        return empty
    }

    override fun onDisable() {

    }
}