package god.allah.api.setting.types

import god.allah.api.setting.ISetting

class SettingCategory(vararg settings: ISetting) : ISetting() {
    var expanded = false
}