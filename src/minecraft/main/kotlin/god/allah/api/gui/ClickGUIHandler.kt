package god.allah.api.gui

import god.allah.api.Registry
import god.allah.api.executors.Category
import god.allah.api.executors.Module

object ClickGUIHandler {
    val modules = HashMap<Category, ArrayList<Module>>()

    fun init() {
        Registry.getEntries(Module::class.java).forEach { module ->
            val category = module.category
            if(!modules.containsKey(category))
                modules[category] = ArrayList()
            modules[category]?.add(module)
        }
    }
}