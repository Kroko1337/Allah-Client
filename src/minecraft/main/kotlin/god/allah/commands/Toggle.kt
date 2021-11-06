package god.allah.commands

import god.allah.api.Registry
import god.allah.api.executors.Command
import god.allah.api.executors.CommandInfo
import god.allah.main.Wrapper.getModule
import god.allah.main.Wrapper.sendMessage

@CommandInfo("toggle", ["t"])
class Toggle : Command() {
    override fun execute(args: Array<String>): Boolean {
        when(args.size) {
            1 -> {
                val module = getModule(args[0])
                if(module != null) {
                    module.toggle()
                    sendMessage((if(module.isToggled()) "§a" else "§c") + "Toggled §e${module.name}")
                } else {
                    sendMessage("§e${args[0]} §cis not a valid Module!")
                }
                return true
            }
        }
        return false
    }

    override fun getArguments(): Array<String> {
        return arrayOf("[Module]")
    }
}