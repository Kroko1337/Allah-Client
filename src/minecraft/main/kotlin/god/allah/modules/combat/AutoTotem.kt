package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.utils.findItem
import god.allah.events.UpdateEvent
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.network.play.client.CPacketClickWindow

@Module.Info("AutoTotem", Category.COMBAT)
class AutoTotem : Module() {

    @Value("Only when needed")
    var onlyWhenNeeded = CheckBox(false)

    @EventInfo
    override fun onEvent(event: Event) {
        when(event) {
            is UpdateEvent -> {
                if(player.heldItemOffhand.item != Items.TOTEM_OF_UNDYING && (player.health <= 4 || !onlyWhenNeeded.value)) {
                    val searchItem = findItem(Items.TOTEM_OF_UNDYING, player.inventory)
                    if (searchItem != -1) {
                        val transactionId = player.inventoryContainer.getNextTransactionID(player.inventory);
                        sendPacket(CPacketClickWindow(0, if(searchItem < 9) searchItem + 36 else searchItem, 0, ClickType.PICKUP, player.inventory.getStackInSlot(searchItem), transactionId))
                        sendPacket(CPacketClickWindow(0, 45, 0, ClickType.PICKUP, player.inventory.getStackInSlot(searchItem), transactionId))
                    }
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}