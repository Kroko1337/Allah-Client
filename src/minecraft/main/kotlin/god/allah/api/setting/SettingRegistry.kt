package god.allah.api.setting

import god.allah.api.Executor
import god.allah.api.Registry
import god.allah.api.event.EventHandler
import god.allah.api.executors.Module
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.scanners.MethodAnnotationsScanner

object SettingRegistry {
    val values = HashMap<Module, ArrayList<ISetting<*>>>()
    private val reflection = Reflections("god.allah.modules")

    fun init() {
        reflection.getTypesAnnotatedWith(Module.Info::class.java).forEach { clazz ->
            val module = getModule(clazz.canonicalName)
            if (module != null) {
                if (!values.containsKey(module))
                    values[module] = ArrayList()
                for (field in clazz.declaredFields) {
                    if (field.isAnnotationPresent(Value::class.java)) {
                        field.isAccessible = true
                        val setting = field.get(module)
                        val annotation = field.getAnnotation(Value::class.java)
                        if(setting is ISetting<*>) {
                            setting.name = annotation.name
                            setting.displayName = annotation.displayName
                            setting.module = module
                            values[module]?.add(setting)
                        }
                    }
                }
            }
        }
    }

    fun getSetting(name: String, module: Module) : ISetting<*>? {
        values[module]?.forEach { setting ->
            if(setting.getDisplay().equals(name, true))
                return setting
        }
        return null
    }

    fun getSetting(name: String, module: Class<out Module>) : ISetting<*>? {
        values[Registry.getEntry(module)]?.forEach { setting ->
            if(setting.name.equals(name, true))
                return setting
        }
        return null
    }

    private fun getModule(name: String): Module? {
        Registry.getEntries(Module::class.java).forEach { module ->
            if (module.javaClass.name.equals(name, true))
                return module
        }
        return null
    }
}