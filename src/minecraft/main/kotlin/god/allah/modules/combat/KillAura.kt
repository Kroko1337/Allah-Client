package god.allah.modules.combat

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import god.allah.events.UpdateEvent
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.input.Keyboard

@ModuleInfo("KillAura", Category.COMBAT, defaultKey = Keyboard.KEY_R)
class KillAura : Module() {

    private val range = 4

    var target: Entity? = null

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is UpdateEvent -> {
                for (tar in world.loadedEntityList) {
                    if (shouldAttack(tar)!!) {
                        target = tar
                    }
                }
                if (!shouldAttack(target)!!) {
                    target = null
                }
                target?.let { doAttack(it) }
            }

        }
    }

    private fun doAttack(target : Entity) {
        player.swingingHand
        playerController.attackEntity(player, target)
    }

    private fun shouldAttack(player: Entity?): Boolean? {
        if (player != null) {
            return player is EntityLivingBase && player != player && player.getDistance(player) <= range
        }
        return null
    }


    override fun onEnable() {

    }

    override fun onDisable() {

    }


}