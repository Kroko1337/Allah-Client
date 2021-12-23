package god.allah.api

import god.allah.api.executors.Module
import god.allah.main.Main
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.network.Packet
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import kotlin.properties.Delegates

object Wrapper {
    lateinit var instance: Main

    const val name = "Allah"
    const val version = "v1.0"
    var coder = arrayOf("Kroko", "Felix1337", "Aro")

    val initTime = System.currentTimeMillis()

    const val prefix = "§c$name §7>> §f"
    var commandPrefix = "."

    val fr: FontRenderer get() = mc.fontRenderer

    val mc: Minecraft get() = Minecraft.getMinecraft()
    val player: EntityPlayerSP get() = mc.player


    fun sendPacket(packet: Packet<*>) {
        mc.connection?.networkManager!!.sendPacket(packet)
    }

    fun getModule(name: String) : Module? {
        Registry.getEntries(Module::class.java).forEach { module ->
            if(module.name.equals(name, true))
                return module
        }
        return null
    }

    fun sendMessage(message: Any, actionBar: Boolean = false, style: Style? = null, prefix: Boolean = true) {
        val text = TextComponentString("${if(prefix) Wrapper.prefix else ""}$message")
        if(style != null)
            text.style = style
        player.sendStatusMessage(text, actionBar)
    }

    fun isDeveloperMode() : Boolean {
        return System.getProperty("java.class.path").contains("idea_rt.jar")
    }
}