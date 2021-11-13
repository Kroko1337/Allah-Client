package god.allah.api.setting.types

import god.allah.api.setting.ISetting
import god.allah.draggable.ArrayList

class SettingGroup(vararg settings: ISetting<*>) : ISetting<String>() {
    var expanded = false
    val includedSettings = ArrayList<ISetting<*>>()

    init {
        settings.forEach {
            includedSettings.add(it)
        }
    }
}