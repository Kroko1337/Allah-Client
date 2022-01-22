package god.allah.modules.movement

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.TimeHelper
import god.allah.api.setting.Value
import god.allah.api.setting.types.ComboBox
import god.allah.api.utils.setSpeed
import god.allah.events.NoClipEvent
import god.allah.events.UpdateEvent
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos

@Module.Info("Fly", Category.MOVEMENT)
class Fly : Module() {

    @Value("Mode")
    val mode = ComboBox("Vanilla", arrayOf("Vanilla", "WatchDuck", "Verus"))

    var watchDuckTime = 0

    private val blocksMCTimeHelper = TimeHelper()

    @EventInfo
    override fun onEvent(event: Event) {
        when (mode.value) {
            "Verus" -> {
                when(event) {
                    is UpdateEvent -> {
                        player.motionY = 0.0
                        if(mc.gameSettings.keyBindJump.pressed)
                            player.motionY = 0.05
                        else if(mc.gameSettings.keyBindSneak.pressed)
                            player.motionY = -0.05
                        sendPacket(CPacketPlayerTryUseItemOnBlock(player.position, EnumFacing.UP, EnumHand.MAIN_HAND, 0F, 0F, 0F))
                    }
                }
            }
            "BlocksMC" -> {
                when(event) {
                    is NoClipEvent -> {
                        event.noClip = true
                    }
                    is UpdateEvent -> {
                        player.motionY = 5E-3
                        setSpeed(1.2, onlyWhenPress = true)
                    }
                }
            }
            "Vanilla" -> {
                when (event) {
                    is UpdateEvent -> {
                        player.capabilities.isFlying = true
                    }
                }
            }
            "WatchDuck" -> {
                when (event) {
                    is NoClipEvent -> {
                        if (watchDuckTime < 75)
                            event.noClip = true
                    }
                    is UpdateEvent -> {
                        watchDuckTime++
                        if (watchDuckTime < 75) {
                            player.onGround = true
                            player.motionY = -0.0001
                            setSpeed(0.3)
                        } else {
                            if (watchDuckTime > 90) {
                                watchDuckTime = 0
                                sendPacket(CPacketPlayer.Position(x, y + 4, z, false))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onEnable() {
        watchDuckTime = 0
        blocksMCTimeHelper.reset()
    }

    override fun onDisable() {
        player.capabilities.isFlying = false
        timer.timerSpeed = 1F
    }

}