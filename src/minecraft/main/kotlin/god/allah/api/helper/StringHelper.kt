package god.allah.api.helper

import java.util.*

object StringHelper {
    fun handleQuotation(arguments: Array<String>) : Array<String> {
        var merge = false
        var currentIndex = 0
        var array = arguments.copyOf()

        for(i in array.indices) {
            array[i] = ""
        }
        for(i in arguments.indices) {
            var argument = arguments[i]
            if(argument.startsWith('\"')) {
                argument = argument.substring(1)
                merge = true
            }
            array[currentIndex] += argument + (if(merge) " " else "")
            if(!merge) {
                currentIndex++
            }
            if(argument.endsWith('\"')) {
                merge = false
                array[currentIndex] = array[currentIndex].substring(0, array[currentIndex].length - 2)
                currentIndex++
            }
        }
        array = array.copyOfRange(0, currentIndex)
        return array
    }
}