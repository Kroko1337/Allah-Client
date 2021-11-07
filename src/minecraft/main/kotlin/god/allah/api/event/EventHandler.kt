package god.allah.api.event

import god.allah.api.Registry
import god.allah.api.executors.Module
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object EventHandler {
    val modules = ArrayList<Module>()

    private val priorityList = HashMap<EventPriority, ArrayList<Module>>()
    private val reflection = Reflections("god.allah.modules", MethodAnnotationsScanner())

    fun init() {
        reflection.getMethodsAnnotatedWith(EventInfo::class.java).forEach { method ->
            val eventInfo = method.getAnnotation(EventInfo::class.java)
            if (!priorityList.containsKey(eventInfo.priority))
                priorityList[eventInfo.priority] = ArrayList()
            val module = getModule(method.declaringClass.name)
            if (module != null)
                priorityList[eventInfo.priority]?.add(module)
        }

        priorityList.keys.forEach { key ->
            priorityList[key]?.forEach { module ->
                modules.add(module)
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