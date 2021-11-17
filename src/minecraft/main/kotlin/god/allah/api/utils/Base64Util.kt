package god.allah.api.utils

import java.util.*

fun encode(decoded: String) : String {
    return String(Base64.getEncoder().encode(decoded.encodeToByteArray()))
}

fun decode(encoded: String) : String {
    return String(Base64.getDecoder().decode(encoded))
}