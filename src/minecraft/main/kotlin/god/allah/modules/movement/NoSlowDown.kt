package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.TimeHelper
import god.allah.events.UpdateEvent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.network.play.client.CPacketHeldItemChange
import kotlin.math.roundToLong
import kotlin.random.Random

@Module.Info("NoSlowDown", Category.MOVEMENT)
class NoSlowDown : Module() {

    private val timeHelper = TimeHelper()

    @EventInfo(priority = EventPriority.LOW)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                var f = 0.25f + EnchantmentHelper.getEfficiencyModifier(player).toFloat() * 0.05f

                f += 0.75f

                if (timeHelper.hasReached((1000.0 / 12).roundToLong())) {
                    sendPacket(CPacketHeldItemChange())
                    player.resetCooldown()
                    player.resetActiveHand()
                    timeHelper.reset()
                }

                if (Random.nextFloat() < f) {
                    world.setEntityState(player, 30.toByte())
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }
}