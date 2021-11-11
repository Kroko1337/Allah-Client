package god.allah.api.utils

import java.awt.Color

fun getRainbow(offset: Int, speed: Int, saturation: Float = 1F, brightness: Float = 1F) : Int {
    val hue: Float = ((System.currentTimeMillis() + offset) % speed) / speed.toFloat()
    return Color.HSBtoRGB(hue, saturation, brightness)
}