package god.allah.draggable

import god.allah.api.Registry
import god.allah.api.Resolution
import god.allah.api.Wrapper
import god.allah.api.executors.Category
import god.allah.api.executors.Draggable
import god.allah.api.executors.Module
import god.allah.api.utils.getRainbow
import god.allah.api.setting.SettingRegistry
import god.allah.modules.gui.HUD
import net.minecraft.client.gui.Gui
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max

@Draggable.Info("ArrayList", 1, 20)
class ArrayList : Draggable() {

    override fun isVisible(): Boolean {
        return Registry.getEntry(HUD::class.java).isToggled()
    }

    override fun draw() {
        var yAxis = yPos.toFloat()
        var calcWidth = 10

        val right = xPos >= Resolution.widthD / 2
        val down = yPos >= Resolution.heightD / 2


        if (down)
            yAxis -= fr.FONT_HEIGHT
        val suffix: Boolean = SettingRegistry.getSetting("Suffix", HUD::class.java)?.getField("Value") as Boolean
        Registry.getEntries(Module::class.java)
            .filter { module -> module.isToggled() && module.category != Category.GUI }
            .sortedWith(Comparator.comparingInt { module -> -fr.getStringWidth(module.getDisplay(suffix)) })
            .forEach { module ->
                var position = xPos.toFloat()
                val name = module.getDisplay(suffix)
                if (calcWidth < fr.getStringWidth(name))
                    calcWidth = fr.getStringWidth(name)

                if (right) {
                    hitBoxX = xPos - calcWidth
                    position -= fr.getStringWidth(name)
                } else {
                    hitBoxX = xPos
                }
                fr.drawStringWithShadow(name, position, yAxis, -1)
                if (down)
                    yAxis -= fr.FONT_HEIGHT
                else
                    yAxis += fr.FONT_HEIGHT
            }
        width = calcWidth
        hitBoxY = if (yAxis < yPos) {
            abs(yAxis.toInt())
        } else {
            yPos
        }

        height = abs(yAxis.toInt() - yPos)
    }
}