package god.allah.commands

import god.allah.api.executors.Command
import god.allah.api.executors.CommandInfo
import god.allah.api.setting.ISetting
import god.allah.api.setting.SettingRegistry
import god.allah.main.Wrapper

@CommandInfo("setting", ["value", "val"])
class Setting : Command() {

    override fun execute(args: Array<String>): Boolean {
        when (args.size) {
            1 -> {
                val module = Wrapper.getModule(args[0])
                if (module != null) {
                    clearChat()
                    SettingRegistry.values[module]?.forEach { setting ->
                        if (setting.getField("value") != null)
                            sendMessage("§e${setting.name} §7-> §b${setting.getField("value")}")
                    }
                } else {
                    sendMessage("§e${args[0]} §cis not a valid Module!")
                }
            }
            2 -> {
                val module = Wrapper.getModule(args[0])
                if (module != null) {
                    clearChat()
                    val setting = SettingRegistry.getSetting(args[1], module)
                    if (setting != null) {
                        sendMessage("§e${setting.name} §7-> §b${setting.getField("value")}")
                        if (setting.getField("modes") != null) {
                            val modes: Array<*> = setting.getField("modes") as Array<*>
                            sendMessage("")
                            sendMessage("§eAvailable modes:")
                            for (mode in modes) {
                                sendMessage("§b$mode")
                            }
                        }
                    } else {
                        sendMessage("§e${args[1]} §cis not a valid Setting!")
                    }
                } else {
                    sendMessage("§e${args[0]} §cis not a valid Module!")
                }
            }
            3 -> {
                val module = Wrapper.getModule(args[0])
                if (module != null) {
                    clearChat()
                    val setting = SettingRegistry.getSetting(args[1], module)
                    if (setting != null) {
                        try {
                            if (setting.getField("modes") != null) {
                                var exist = false
                                val modes: Array<*> = setting.getField("modes") as Array<*>
                                for (mode in modes) {
                                    if (args[2] == mode) {
                                        exist = true
                                        break
                                    }
                                    if (mode is String) {
                                        if (args[2].equals(mode, true)) {
                                            exist = true
                                            args[2] = mode
                                            break
                                        }
                                    }
                                }
                                if (!exist) {
                                    sendMessage("§e${args[2]} §cis not a valid Mode!")
                                    val similarModes = ArrayList<String>()
                                    for (mode in modes) {
                                        if(mode is String) {
                                            if(mode.lowercase().startsWith(args[2].lowercase()))
                                                similarModes.add(mode)
                                        } else {
                                            break
                                        }
                                    }
                                    if(similarModes.isNotEmpty()) {
                                        sendMessage("")
                                        sendMessage("§e§lSimilar modes:")
                                        for(mode in similarModes) {
                                            sendMessage("§b$mode")
                                        }
                                    }
                                    return true
                                }
                            }
                            setting.tryChangeField("value", args[2])
                            sendMessage("§e${setting.name} §7-> §b${setting.getField("value")}")
                        } catch (ex: Exception) {
                            sendMessage("§e§l${args[2]} §cis not an valid Value!")
                        }
                    } else {
                        sendMessage("§e${args[1]} §cis not a valid Setting!")
                    }
                } else {
                    sendMessage("§e${args[0]} §cis not a valid Module!")
                }
            }
            else -> {
                return false
            }
        }
        return true
    }
}