package god.allah.main

import god.allah.main.Wrapper.instance
import god.allah.main.Wrapper.name
import god.allah.main.Wrapper.version
import org.lwjgl.opengl.Display

class Main {

    fun onStart() {
        instance = this
        Display.setTitle("$name $version | Minecraft 1.12.2")
        Runtime.getRuntime().addShutdownHook(Thread { onShutdown() })
    }

    private fun onShutdown() {

    }
}