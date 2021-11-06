package god.allah.api.executors

import god.allah.api.Executor

abstract class Command : Executor {

    val name: String
    val aliases: Array<String>

    init {
        val commandInfo = this.javaClass.getAnnotation(CommandInfo::class.java)
        name = commandInfo.name
        aliases = commandInfo.aliases
    }

    abstract fun execute(args: Array<String>) : Boolean
}

annotation class CommandInfo(val name: String, val aliases: Array<String> = [])