package god.allah.api.gui

import god.allah.api.Resolution

abstract class IClickGUI(var panelWidth: Double, var panelHeight: Double) {
    abstract fun init()

    abstract fun drawPanel(resolution: Resolution)
}