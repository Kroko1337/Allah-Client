package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.TimeHelper
import god.allah.events.UpdateEvent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.src.Reflector
import net.minecraft.util.EnumHand
import kotlin.math.roundToLong
import kotlin.random.Random

@Module.Info("NoSlowDown", Category.MOVEMENT)
class NoSlowDown : Module() {

    @EventInfo(priority = EventPriority.LOW)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                if (player.getHeldItem(EnumHand.MAIN_HAND).item is ItemSword) {
                    sendPacket(CPacketHeldItemChange())
                    player.movementInput.moveForward *= 0.4f
                    player.movementInput.moveStrafe *= 0.4f
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}