package god.allah.api

import net.minecraft.client.Minecraft
import net.minecraft.util.math.MathHelper
import kotlin.properties.Delegates

object Resolution {
    var width = 1
    var height = 1
    var widthD = 1.0
    var heightD = 1.0
    var scaleFactor = 1

    fun <T : Resolution?> resize(mc : Minecraft) : T{
        width = mc.displayWidth
        height = mc.displayHeight
        scaleFactor = 1
        val flag = mc.isUnicode
        var i = mc.gameSettings.guiScale

        if (i == 0) {
            i = 1000
        }

        while (this.scaleFactor < i && this.width / (this.scaleFactor + 1) >= 320 && this.height / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor
        }

        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor
        }

        this.widthD = this.width.toDouble() / this.scaleFactor.toDouble()
        this.heightD = this.height.toDouble() / this.scaleFactor.toDouble()
        this.width = MathHelper.ceil(this.widthD)
        this.height = MathHelper.ceil(this.heightD)
        return this as T
    }
}