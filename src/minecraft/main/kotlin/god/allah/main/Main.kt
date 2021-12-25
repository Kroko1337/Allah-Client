package god.allah.main

import god.allah.api.Registry
import god.allah.api.Wrapper.clickGUI
import god.allah.api.event.EventHandler
import god.allah.api.setting.SettingRegistry
import god.allah.api.Wrapper.coder
import god.allah.api.Wrapper.instance
import god.allah.api.Wrapper.name
import god.allah.api.Wrapper.version
import god.allah.api.auth.handle.init
import god.allah.api.clickgui.ClickGUI
import org.lwjgl.opengl.Display
import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

class Main {

    fun onStart() {
        init()
        instance = this
        Display.setTitle("$name $version | Minecraft 1.12.2 | coded by ${coder[0] + " & " + coder[1]}")
        Registry.init()
        EventHandler.init()
        SettingRegistry.init()

        clickGUI = ClickGUI()

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