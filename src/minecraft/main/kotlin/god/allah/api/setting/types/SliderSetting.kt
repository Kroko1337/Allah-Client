package god.allah.api.setting.types

import god.allah.api.setting.Dependency
import god.allah.api.setting.ISetting

class SliderSetting<T> (var value: T, val min: T, val max: T, vararg dependencies: Dependency<*> = arrayOf()) : ISetting<T>(*dependencies) {
}