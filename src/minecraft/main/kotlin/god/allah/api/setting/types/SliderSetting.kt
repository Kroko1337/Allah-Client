package god.allah.api.setting.types

import god.allah.api.setting.ISetting

class SliderSetting<T> (val value: T, val min: T, val max: T) : ISetting {
}