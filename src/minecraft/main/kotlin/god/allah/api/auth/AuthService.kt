package god.allah.api.auth

import java.security.SecureRandom
import java.security.cert.X509Certificate
import god.allah.api.auth.handle.allTrustFactory
import god.allah.api.auth.handle.allTrust
import javax.net.ssl.*

enum class AuthService(val authServer: String, val sessionServer: String, val sslSocketFactory: SSLSocketFactory) {
    MOJANG("https://authserver.mojang.com/", "https://sessionserver.mojang.com/", HttpsURLConnection.getDefaultSSLSocketFactory()),
    ALTENING("http://authserver.thealtening.com/", "http://sessionserver.thealtening.com/", allTrustFactory.socketFactory);

    init {
        allTrustFactory.init(
            null as Array<KeyManager?>?,
            allTrust,
            SecureRandom()
        )
    }
}