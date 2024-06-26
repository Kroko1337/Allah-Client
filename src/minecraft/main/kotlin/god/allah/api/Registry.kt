package god.allah.api

import god.allah.api.event.EventHandler
import god.allah.api.executors.Category
import god.allah.api.executors.Draggable
import god.allah.api.executors.Command
import god.allah.api.executors.Module
import org.reflections.Reflections
import java.util.Comparator

object Registry {
    val executors = HashMap<Class<out Executor>, ArrayList<Executor>>()

    private val reflection = Reflections("god.allah")

    fun init() {
        addExecutor(Module::class.java, Module.Info::class.java)
        addExecutor(Command::class.java, Command.Info::class.java)
        addExecutor(Draggable::class.java, Draggable.Info::class.java)
        handle()
    }

    fun handle() {
        executors.keys.forEach { executor ->
            val list: ArrayList<Executor> = executors[executor]?.clone() as ArrayList<Executor>
            when(executor) {
                Module::class.java -> {
                    for(i in executors[executor]?.indices!!) {
                        when(val mod = list[i]) {
                            is Module -> {
                                if(mod.category == Category.DEBUG && !Wrapper.isDeveloperMode()) {
                                    executors[executor]?.remove(mod)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun <T : Executor?> getEntry(executor: Class<out T>): T {
        executors.keys.forEach { keys ->
            getEntries(keys).forEach { entry ->
                if (entry.javaClass == executor)
                    return entry as T
            }
        }
        return null as T
    }

    fun <T : Executor?> getEntries(executor: Class<T>): ArrayList<T> {
        return executors[executor] as ArrayList<T>
    }

    private fun addExecutor(executorMain: Class<out Executor>, annotation: Class<out Annotation>) {
        if (!executors.containsKey(executorMain))
            executors[executorMain] = ArrayList()
        reflection.getTypesAnnotatedWith(annotation).forEach { executor ->
            if (executors[executorMain] != null) {
                executors[executorMain]?.add(executor.newInstance() as Executor)
            }
        }
        executors[executorMain]?.sortWith(Comparator.comparing { executor: Executor -> executor.javaClass.simpleName})
    }
}