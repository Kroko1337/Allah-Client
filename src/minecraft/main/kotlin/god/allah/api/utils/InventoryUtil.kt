package god.allah.api.utils

import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import god.allah.api.Wrapper.mc
import javassist.expr.Cast
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import java.lang.ref.Reference

fun getAmount(item: Item, inventory: IInventory) : Int {
    var amount = 0
    for(i in 0 ..inventory.sizeInventory) {
        val stack: ItemStack? = inventory.getStackInSlot(i)
        if(stack != null && stack.item == item)
            amount += stack.count
    }
    return amount
}

fun getHotBarSlot(item: Item) : Int {
    for(i in 0 ..9) {
        val stack: ItemStack? = mc.player.inventory.getStackInSlot(i)
        if(stack != null && stack.item == item)
            return i
    }
    return -1
}

inline fun <reified T: Item> getHotBarSlotClassified() : Int {
    for(i in 0 ..9) {
        val stack: ItemStack? = mc.player.inventory.getStackInSlot(i)
        if(stack != null && stack.item is T)
            return i
    }
    return -1
}

fun findItem(item: Item, inventory: IInventory) : Int {
    for(i in 0 ..inventory.sizeInventory) {
        val stack: ItemStack? = inventory.getStackInSlot(i)
        if(stack != null && stack.item == item)
            return i
    }
    return -1
}