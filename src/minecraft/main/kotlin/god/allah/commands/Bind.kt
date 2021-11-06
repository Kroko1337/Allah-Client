package god.allah.commands

import god.allah.api.executors.Command
import god.allah.api.executors.CommandInfo
import god.allah.main.Wrapper
import org.lwjgl.input.Keyboard

@CommandInfo("bind")
class Bind : Command() {
    override fun execute(args: Array<String>): Boolean {
        when (args.size) {
            2 -> {
                val module = Wrapper.getModule(args[0])
                if (module != null) {
                    module.keyBind = Keyboard.getKeyIndex(args[1].uppercase())
                    sendMessage("§e${module.name} §abounded to §e§l${args[1].uppercase()}")
                } else {
                    sendMessage("§e${args[0]} §cis not a valid Module!")
                }
                return true
            }
            1 -> {
                val module = Wrapper.getModule(args[0])
                if (module != null) {
                    if (module.keyBind == 0)
                        sendMessage("§e§l${module.name} §cis not bounded!")
                    else
                        sendMessage("§e§l${module.name} §ais currently bounded to §e§l${Keyboard.getKeyName(module.keyBind)}")
                } else {
                    sendMessage("§e${args[0]} §cis not a valid Module!")
                }
                return true
            }
        }
        return false
    }

    override fun getArguments(): Array<String> {
        return arrayOf("[Module] {Key}")
    }
}