package god.allah.api.executors

import god.allah.api.Executor
import god.allah.api.event.Event
import god.allah.api.setting.ISetting

abstract class Module : Executor {
    val name: String
    val category: Category
    var keyBind: Int

    private var toggled = false

    init {
        val moduleInfo = this.javaClass.getAnnotation(Info::class.java)
        name = moduleInfo.name
        category = moduleInfo.category
        keyBind = moduleInfo.defaultKey
    }

    abstract fun onEvent(event: Event)
    abstract fun onEnable()
    abstract fun onDisable()

    fun setToggled(toggled: Boolean) {
        this.toggled = toggled
        when(toggled) {
            true -> {
                onEnable()
            }
            else -> {
                onDisable()
            }
        }
    }

    fun toggle() {
        setToggled(!isToggled())
    }

    fun isToggled() : Boolean {
        return toggled
    }

    open fun getInfo() : String? {
        return null
    }

    fun getDisplay(suffix: Boolean) : String {
        return name + (if(this.getInfo() != null && suffix) " §7" + this.getInfo() else "")
    }

    annotation class Info(val name: String, val category: Category, val defaultKey: Int = 0)
}

enum class Category(name: String) {
    VISUAL("Visual"), PLAYER("Player"), WORLD("World"), MOVEMENT("Movement"), COMBAT("Combat"), MISC("Misc"), GUI("Gui"), DEBUG("Debug")
}