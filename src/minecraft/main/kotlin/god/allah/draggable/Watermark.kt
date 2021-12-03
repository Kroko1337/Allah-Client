package god.allah.draggable

import god.allah.api.Registry
import god.allah.api.executors.Draggable
import god.allah.api.setting.SettingRegistry
import god.allah.api.setting.types.ComboBox
import god.allah.api.utils.drawImage
import god.allah.modules.gui.HUD
import net.minecraft.util.ResourceLocation

@Draggable.Info("Watermark", 1, 1)
class Watermark : Draggable() {

    private var hud: HUD? = null
    private var watermarkMode: ComboBox<String>? = null

    override fun draw() {
        if (watermarkMode == null || hud == null) {
            hud = Registry.getEntry(HUD::class.java)
            watermarkMode = SettingRegistry.getSetting("Watermark Mode", hud!!) as ComboBox<String>
        } else
            if (hud!!.isToggled())
                when (watermarkMode!!.value) {
                    "Holo" -> {
                        drawImage(ResourceLocation("allah/textures/gui/holo_logo.png"), xPos, yPos, 48F, 16F)
                        width = 48
                        height = 16
                    }
                    "Allah" -> {
                        drawImage(ResourceLocation("allah/textures/gui/allah_logo.png"), xPos, yPos, 120F, 66F)
                        width = 120
                        height = 66
                    }
                }
    }
}