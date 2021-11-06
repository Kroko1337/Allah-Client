package god.allah.api.event

import god.allah.api.Executor
import god.allah.api.Registry
import god.allah.api.executors.Module

open class Event {
    var canceled = false

    fun <T : Event?> onFire(): T {
        for (module in EventHandler.modules) {
            if (module.isToggled())
                module.onEvent(this)
        }
        return this as T
    }
}