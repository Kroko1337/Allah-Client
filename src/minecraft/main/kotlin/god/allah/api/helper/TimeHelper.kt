package god.allah.api.helper

class TimeHelper {
    private var currentMS = System.currentTimeMillis()

    fun hasReached(time: Long, reset: Boolean = true) : Boolean {
        val reached = System.currentTimeMillis() - currentMS >= time
        if(reached && reset)
            reset()
        return reached
    }

    fun reset() {
        currentMS = System.currentTimeMillis()
    }
}