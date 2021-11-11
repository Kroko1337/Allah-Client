package god.allah.draggable

import god.allah.api.Registry
import god.allah.api.Wrapper
import god.allah.api.executors.Category
import god.allah.api.executors.Draggable
import god.allah.api.executors.Module
import god.allah.api.utils.getRainbow
import god.allah.api.setting.SettingRegistry
import god.allah.modules.gui.HUD
import kotlin.math.max

@Draggable.Info("ArrayList", 3, 3)
class ArrayList : Draggable() {

    override fun draw() {
        var yAxis = yPos.toFloat()
        var calcWidth = 10
        val suffix: Boolean = SettingRegistry.getSetting("Suffix", HUD::class.java)?.getField("Value") as Boolean
        Registry.getEntries(Module::class.java)
            .filter { module -> module.isToggled() && module.category != Category.GUI }
            .sortedWith(Comparator.comparingInt { module -> -fr.getStringWidth(module.getDisplay(suffix)) })
            .forEach { module ->
                val name = module.getDisplay(suffix)
                fr.drawStringWithShadow(name, xPos.toFloat(), yAxis.toFloat(), -1)
                if(calcWidth < fr.getStringWidth(name))
                    calcWidth = fr.getStringWidth(name)
                yAxis += fr.FONT_HEIGHT
            }
        width = calcWidth
        height = yAxis.toInt()
    }
}