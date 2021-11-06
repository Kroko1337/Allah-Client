package god.allah.main

import com.thealtening.auth.service.AlteningServiceType
import god.allah.api.Registry
import god.allah.api.event.EventHandler
import god.allah.main.Wrapper.coder
import god.allah.main.Wrapper.instance
import god.allah.main.Wrapper.name
import god.allah.main.Wrapper.version
import org.lwjgl.opengl.Display
import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

class Main {

    fun onStart() {
        instance = this
        Display.setTitle("$name $version | Minecraft 1.12.2 | coded by ${coder[0] + " & " + coder[1]}")
        Registry.init()
        EventHandler.init()

        val tray = SystemTray.getSystemTray()
        val image: Image = Toolkit.getDefaultToolkit().createImage("icon.png")
        val trayIcon = TrayIcon(image, "Tray Demo")
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "System tray icon demo"
        tray.add(trayIcon)
        trayIcon.displayMessage(name, "i have depressions", TrayIcon.MessageType.WARNING)

        Runtime.getRuntime().addShutdownHook(Thread { onShutdown() })
    }

    private fun onShutdown() {

    }
}