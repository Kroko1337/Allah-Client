package god.allah.api.setting.types

import god.allah.api.setting.ISetting

class ComboBox<T> (var value: T, val modes: Array<T>) : ISetting() {
    var expanded = false
}