package god.allah.api.utils

import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URL
import javax.imageio.ImageIO
import god.allah.api.Wrapper.mc
import net.minecraft.client.gui.Gui.*
import org.lwjgl.opengl.GL11.*

fun drawImage(location: ResourceLocation, x: Int, y: Int, width: Float, height: Float) {
    mc.textureManager.bindTexture(location)
    color(1F,1F,1F,1F)
    glEnable(GL_BLEND)
    drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, width.toInt(), height.toInt(), width, height)
    glDisable(GL_BLEND)
}

fun drawImage(location: ResourceLocation, scale: Float, x: Float, y: Float, width: Float, height: Float) {
    val scaleMulti = 1 / scale
    pushMatrix()
    mc.textureManager.bindTexture(location)
    glColor4f(1F, 1F, 1F, 1F)
    glEnable(GL_BLEND)
    glScalef(scale, scale, scale)
    drawModalRectWithCustomSizedTexture(
        (x * scaleMulti).toInt(),
        (y * scaleMulti).toInt(),
        0F,
        0F,
        width.toInt(),
        height.toInt(),
        width,
        height
    )
    glDisable(GL_BLEND)
    popMatrix()
}

fun generateYouTubeThumbnail(link: String): String {
    val url = URL(link)
    val uri = URI(url.protocol, url.userInfo, url.host, url.port, url.path, url.query, url.ref)
    val generated = uri.query.replace("v=", "")
    return "http://img.youtube.com/vi/$generated/0.jpg"
}

fun getPicture(link: String) : ResourceLocation? {
    val bufferedImage = ImageIO.read(URL(link))
    var resourceLocation: ResourceLocation? = null
    mc.addScheduledTask {
        val dynamicTexture = DynamicTexture(bufferedImage)
        resourceLocation = mc.textureManager.getDynamicTextureLocation("cover.jpg", dynamicTexture)
    }
    return resourceLocation
}