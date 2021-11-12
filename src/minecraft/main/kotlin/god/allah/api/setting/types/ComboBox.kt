package god.allah.api.setting.types

import god.allah.api.setting.ISetting

class ComboBox<T>(var value: T, val modes: Array<T>) : ISetting<T>() {
    var expanded = false

    init {
        if (value is String)
            modes.sortWith(Comparator.comparing { it.toString() })
    }
}