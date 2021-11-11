package god.allah.modules.gui

import god.allah.api.Registry
import god.allah.api.Wrapper
import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.utils.getRainbow
import god.allah.events.Render2DEvent
import net.minecraft.util.math.MathHelper
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.awt.Toolkit
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max


@Module.Info("HUD", Category.GUI)
class HUD : Module() {

    @Value("Suffix")
    var suffix = CheckBox(true)

    var color = 0

    init {
        setToggled(true)
    }

    @EventInfo
    override fun onEvent(event: Event) {
        when (event) {
            is Render2DEvent -> {
                var yAxis = 3

                for(i in 0 ..Wrapper.name.length) {
                    val char = Wrapper.name.substring(max(i - 1, 0), i)
                    fr.drawStringWithShadow(char,
                        event.resolution.width / 2F - fr.getStringWidth(Wrapper.name) / 2F + fr.getStringWidth(Wrapper.name.substring(0, max(i - 1, 0))), fr.FONT_HEIGHT.toFloat(), getRainbow(i * 200, 4600, saturation = 0.7F))
                }
            }
        }
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }

}