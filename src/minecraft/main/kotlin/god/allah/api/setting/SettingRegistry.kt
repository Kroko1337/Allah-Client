package god.allah.api.setting

import god.allah.api.Registry
import god.allah.api.event.EventHandler
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.scanners.MethodAnnotationsScanner

object SettingRegistry {
    val values = HashMap<Module, ArrayList<ISetting>>()
    private val reflection = Reflections("god.allah.modules")

    fun init() {
        reflection.getTypesAnnotatedWith(ModuleInfo::class.java).forEach { clazz ->
            val module = getModule(clazz.canonicalName)
            if (module != null) {
                if (!values.containsKey(module))
                    values[module] = ArrayList()
                for (field in clazz.declaredFields) {
                    if (field.isAnnotationPresent(Value::class.java)) {
                        field.isAccessible = true
                        val setting: ISetting = field.get(module) as ISetting
                        val annotation = field.getAnnotation(Value::class.java)
                        setting.name = annotation.name
                        setting.displayName = annotation.displayName
                        setting.module = module
                        values[module]?.add(setting)
                    }
                }
            }
        }
    }

    private fun getModule(name: String): Module? {
        Registry.getEntries(Module::class.java).forEach { module ->
            if (module.javaClass.name.equals(name, true))
                return module
        }
        return null
    }
}