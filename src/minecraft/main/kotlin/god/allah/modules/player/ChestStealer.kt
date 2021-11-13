package god.allah.modules.player

import god.allah.api.event.Event
import god.allah.api.event.EventInfo
import god.allah.api.event.EventPriority
import god.allah.api.executors.Category
import god.allah.api.executors.Module
import god.allah.api.helper.TimeHelper
import god.allah.api.setting.Value
import god.allah.api.setting.types.CheckBox
import god.allah.api.setting.types.SettingGroup
import god.allah.api.setting.types.SliderSetting
import god.allah.api.utils.getAmount
import god.allah.api.utils.randomGaussian
import god.allah.events.GuiHandleEvent
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemStack
import java.util.*


@Module.Info("ChestStealer", Category.PLAYER)
class ChestStealer : Module() {

    @Value("Start Delay")
    var startDelay = SliderSetting(150L, 0L, 500L)

    @Value("Grab Delay")
    var grabDelay = SliderSetting(150L, 0L, 500L)

    @Value("Auto Close")
    var autoClose = CheckBox(true)

    @Value("Stack Items")
    var stackItems = CheckBox(true)

    @Value("Random Pick")
    var randomPick = CheckBox(false)

    @Value("Delays")
    var delayGroup = SettingGroup(startDelay, grabDelay, autoClose)

    @Value("Interaction")
    var interactionGroup = SettingGroup(stackItems, randomPick)

    private val startTimer = TimeHelper()
    private val grabTimer = TimeHelper()
    private val items = ArrayList<Int>()

    @EventInfo(priority = EventPriority.HIGH)
    override fun onEvent(event: Event) {
        when (event) {
            is GuiHandleEvent -> {
                when (mc.currentScreen) {
                    is GuiChest -> {
                        if (startTimer.hasReached(startDelay.value + randomGaussian(20.0).toLong(), false)) {
                            items.clear()
                            when (val container = player.openContainer) {
                                is ContainerChest -> {
                                    val chest = container.lowerChestInventory
                                    for (i in 0..chest.sizeInventory) {
                                        val stack: ItemStack? = chest.getStackInSlot(i)
                                        if (stack != null && stack.item != Items.AIR)
                                            items.add(i)
                                    }
                                    if (randomPick.value)
                                        items.shuffle()
                                    var empty = true
                                    for (item in 0 until items.size) {
                                        val i = items[(item - 1).coerceAtLeast(0)]
                                        val stack: ItemStack? = chest.getStackInSlot(i)
                                        if (stack != null)
                                            if (grabTimer.hasReached(
                                                    grabDelay.value + randomGaussian(20.0).toLong(),
                                                    false
                                                )
                                            ) {
                                                if (stackItems.value && stack.count < stack.maxStackSize && stack.maxStackSize != 1 && getAmount(
                                                        stack.item,
                                                        chest
                                                    ) > 1
                                                ) {
                                                    playerController.windowClick(
                                                        container.windowId,
                                                        i,
                                                        0,
                                                        ClickType.PICKUP,
                                                        player
                                                    )
                                                    playerController.windowClick(
                                                        container.windowId,
                                                        i,
                                                        0,
                                                        ClickType.PICKUP_ALL,
                                                        player
                                                    )
                                                    playerController.windowClick(
                                                        container.windowId,
                                                        i,
                                                        0,
                                                        ClickType.PICKUP,
                                                        player
                                                    )
                                                    playerController.windowClick(
                                                        container.windowId,
                                                        i,
                                                        0,
                                                        ClickType.QUICK_MOVE,
                                                        player
                                                    )
                                                } else {
                                                    playerController.windowClick(
                                                        container.windowId,
                                                        i,
                                                        0,
                                                        ClickType.QUICK_MOVE,
                                                        player
                                                    )
                                                }
                                                items.remove(item)
                                                grabTimer.reset()
                                            }
                                        empty = false
                                    }
                                    if (empty && autoClose.value) {
                                        player.closeScreen()
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        startTimer.reset()
                        grabTimer.reset()
                    }
                }
            }
        }
    }

    override fun onEnable() {
    }

    override fun onDisable() {

    }
}