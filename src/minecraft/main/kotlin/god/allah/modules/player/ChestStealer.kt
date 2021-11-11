package god.allah.modules.player

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.TimeHelper
import god.allah.events.GuiHandleEvent
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemAir
import java.lang.NullPointerException


@Module.Info("ChestStealer", Category.PLAYER)
class ChestStealer : Module() {

    private val timeHelper = TimeHelper()

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is GuiHandleEvent -> {

            }
        }
    }

    override fun onEnable() {
    }

    override fun onDisable() {

    }
}