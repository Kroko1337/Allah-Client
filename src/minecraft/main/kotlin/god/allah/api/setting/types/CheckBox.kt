package god.allah.api.setting.types

import god.allah.api.setting.Dependency
import god.allah.api.setting.ISetting

class CheckBox(var value: Boolean, vararg dependencies: Dependency<*> = arrayOf()) : ISetting<Boolean>(*dependencies) {
}