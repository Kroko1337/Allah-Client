package god.allah.api.auth.handle

import com.altening.FieldAdapter
import god.allah.api.auth.AuthService
import java.net.URL
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

var authService = AuthService.MOJANG

private val WHITELISTED_DOMAINS = arrayOf(".minecraft.net", ".mojang.com", ".thealtening.com")
private val minecraftSessionServer = FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService")
private val userAuthentication = FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication")

private val originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
private val originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory()
private val alteningHostVerifier =
    HostnameVerifier { hostname: String, sslSession: SSLSession? -> hostname == "authserver.thealtening.com" || hostname == "sessionserver.thealtening.com" }
private lateinit var alteningFactory: SSLSocketFactory

fun init() {
    minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", WHITELISTED_DOMAINS)
    var sc: SSLContext? = null
    try {
        sc = SSLContext.getInstance("SSL")
        sc.init(
            null, arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate>? {
                        return null
                    }

                    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
                }), SecureRandom()
        )
    } catch (e: Exception) {
        e.printStackTrace();
    }
    alteningFactory = sc!!.socketFactory
}

fun switchTo(auth: AuthService) {
    authService = auth
    when (auth) {
        AuthService.MOJANG -> {
            HttpsURLConnection.setDefaultSSLSocketFactory(originalFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(originalHostVerifier)
        }
        AuthService.ALTENING -> {
            HttpsURLConnection.setDefaultSSLSocketFactory(alteningFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(alteningHostVerifier)
        }
    }

    val authServer = authService.authServer
    val userAuth = userAuthentication
    userAuth.updateFieldIfPresent("BASE_URL", authServer)
    userAuth.updateFieldIfPresent("ROUTE_AUTHENTICATE", URL(authServer + "authenticate"))
    userAuth.updateFieldIfPresent("ROUTE_INVALIDATE", URL(authServer + "invalidate"))
    userAuth.updateFieldIfPresent("ROUTE_REFRESH", URL(authServer + "refresh"))
    userAuth.updateFieldIfPresent("ROUTE_VALIDATE", URL(authServer + "validate"))
    userAuth.updateFieldIfPresent("ROUTE_SIGNOUT", URL(authServer + "signout"))
    val sessionServer: String = authService.sessionServer
    val userSession: FieldAdapter = minecraftSessionServer
    userSession.updateFieldIfPresent("BASE_URL", sessionServer + "session/minecraft/")
    userSession.updateFieldIfPresent("JOIN_URL", URL(sessionServer + "session/minecraft/join"))
    userSession.updateFieldIfPresent("CHECK_URL", URL(sessionServer + "session/minecraft/hasJoined"))
    println("Switched to ${auth.name}")
}