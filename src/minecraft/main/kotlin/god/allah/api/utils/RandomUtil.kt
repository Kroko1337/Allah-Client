package god.allah.api.utils

import java.util.concurrent.ThreadLocalRandom

private val random = ThreadLocalRandom.current()

fun randomGaussian(average: Double) : Double {
    return random.nextGaussian() * average
}

fun random(begin: Int, end: Int) : Int {
    return random.nextInt(begin, end)
}

fun random(begin: Double, end: Double) : Double {
    return random.nextDouble(begin, end)
}

fun random(begin: Float, end: Float) : Float {
    return random.nextDouble(begin.toDouble(), end.toDouble()).toFloat()
}