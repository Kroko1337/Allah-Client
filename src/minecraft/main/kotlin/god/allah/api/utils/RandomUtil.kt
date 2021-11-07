package god.allah.api.utils

import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs
import kotlin.math.sin

private val random = ThreadLocalRandom.current()

fun randomGaussian(average: Double) : Double {
    return random.nextGaussian() * average
}


fun getRandomSin(min: Double, max: Double, timeFactor: Double): Double {
    var random = sin(System.currentTimeMillis().toDouble() / timeFactor) * (max - min)
    if (random < 0.0) {
        random = abs(random)
    }
    return random + min
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