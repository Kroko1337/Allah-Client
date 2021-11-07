package god.allah.api.setting

import god.allah.api.executors.Module

open class ISetting {
    lateinit var module: Module
    lateinit var name: String
    lateinit var displayName: String
}