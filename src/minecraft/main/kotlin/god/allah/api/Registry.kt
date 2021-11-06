package god.allah.api

import god.allah.api.executors.Command
import god.allah.api.executors.CommandInfo
import god.allah.api.executors.Module
import god.allah.api.executors.ModuleInfo
import org.reflections.Reflections

object Registry {
    val executors = HashMap<Class<out Executor>, ArrayList<Executor>>()

    private val reflection = Reflections("god.allah")

    fun init() {
        addExecutor(Module::class.java, ModuleInfo::class.java)
        addExecutor(Command::class.java, CommandInfo::class.java)
    }

    fun <T: Executor?> getEntry(executor: Class<T>) : T {
        executors.keys.forEach { keys ->
            getEntries(keys).forEach { entry ->
                if(entry.javaClass == executor)
                    return executor as T
            }
        }
        return null as T
    }

    fun <T: Executor?> getEntries(executor: Class<T>) : ArrayList<T> {
        return executors[executor] as ArrayList<T>
    }

    private fun addExecutor(executorMain: Class<out Executor>, annotation: Class<out Annotation>) {
        if(!executors.containsKey(executorMain))
            executors[executorMain] = ArrayList()
        reflection.getTypesAnnotatedWith(annotation).forEach { executor ->
            if(executors[executorMain] != null) {
                executors[executorMain]?.add(executor.newInstance() as Executor)
            }
        }
    }
}