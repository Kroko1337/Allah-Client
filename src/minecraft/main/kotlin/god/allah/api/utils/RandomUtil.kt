package god.allah.api.utils

import java.util.concurrent.ThreadLocalRandom

private val random = ThreadLocalRandom.current()

fun randomGaussian(average: Double) : Double {
    return random.nextGaussian() * average
}