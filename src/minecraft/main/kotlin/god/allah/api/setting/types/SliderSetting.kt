package god.allah.api.setting.types

import god.allah.api.setting.ISetting

class SliderSetting<T> (var value: T, val min: T, val max: T) : ISetting<T>() {
}