package god.allah.api.executors

import god.allah.api.Executor
import god.allah.api.Resolution

abstract class Draggable : Executor {
    private var calcX = 0.0
    private var calcY = 0.0

    var dragged = false

    var xPos: Int
    var yPos: Int

    var hitBoxX: Int
    var hitBoxY: Int

    var width: Int
    var height: Int

    val name: String

    init {
        val info = this::class.java.getAnnotation(Info::class.java)
        xPos = info.xPos
        yPos = info.yPos
        hitBoxX = info.yPos
        hitBoxY = info.yPos
        width = info.width
        height = info.height
        name = info.name
    }

    open fun isVisible() : Boolean {
        return true
    }

    abstract fun draw()

    fun savePositions() {
        calcX = xPos / Resolution.widthD
        calcY = yPos / Resolution.heightD
    }

    fun calculatePositions(width: Double, height: Double) {
        xPos = (calcX * width).toInt()
        yPos = (calcY * height).toInt()
    }

    annotation class Info(
        val name: String,
        val xPos: Int,
        val yPos: Int,
        val width: Int = 0,
        val height: Int = 0
    )
}