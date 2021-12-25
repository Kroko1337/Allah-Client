package god.allah.api.clickgui.list

import god.allah.api.Registry.getEntries
import god.allah.api.clickgui.AbstractClickGUI
import god.allah.api.clickgui.list.underlist.ModuleRendering
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.Wrapper
import net.minecraft.client.gui.Gui
import java.util.function.Consumer


class Panel (var x: Int, var y: Int, private var categories: Category) : AbstractClickGUI() {

    private var dragging: Boolean = false

    var extended: Boolean = false

    private var dragX: Int = 0
    private var dragY: Int = 0

    private var mouseX : Int = 0
    private var mouseY : Int = 0
    val modules = ArrayList<ModuleRendering>()

    init {
        for (module in getEntries(Module::class.java)) {
            if (module.category !== this.categories) continue
            modules.add(ModuleRendering(module))
        }

    }

    override fun drawPanel(mouseX: Int, mouseY: Int) {
        if (dragging) {
            this.x = mouseX + dragX
            this.y = mouseY + dragY
        }

        this.mouseX = mouseX
        this.mouseY = mouseY

        val text: String = categories.name
        Gui.drawRect(x, y, (x + width), (y + height), Int.MIN_VALUE)
        Wrapper.fr.drawStringWithShadow(text, calculateMiddle(text, x, width), (y + height - 11).toFloat(), -1)

        if (extended) {
            var yAxis = (0 + height)
            for (list in modules!!) {
                list.drawModules(width, height, x, y + yAxis)
                yAxis += height
            }
        }


    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val mouseOver = isOver(x, y, width, height, mouseX, mouseY)
        if (mouseOver) {
            if (mouseButton == 0) {
                this.dragX = x - mouseX
                this.dragY = y - mouseY
                this.dragging = true
            } else if (mouseButton == 1) {
                this.extended = !extended
            }
        }
        if (extended) modules!!.forEach(Consumer { moduleButton: ModuleRendering -> moduleButton.mouseClicked(mouseX, mouseY, mouseButton) })

    }

    override fun mouseReleased() {
        dragging = false
        if (extended) modules!!.forEach(Consumer { moduleButton: ModuleRendering -> moduleButton.mouseReleased() })
    }
}