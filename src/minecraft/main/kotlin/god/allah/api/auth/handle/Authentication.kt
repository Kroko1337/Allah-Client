package god.allah.api.auth.handle

import com.altening.FieldAdapter
import god.allah.api.auth.AuthService
import java.net.URL

var authService = AuthService.MOJANG

private val WHITELISTED_DOMAINS = arrayOf(".minecraft.net", ".mojang.com", ".thealtening.com")
private val minecraftSessionServer = FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService")
private val userAuthentication = FieldAdapter("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication")

fun switchTo(auth: AuthService) {
    authService = auth
    updateCertificateValidation(authService)
    val authServer = authService.authServer
    minecraftSessionServer.updateFieldIfPresent("WHITELISTED_DOMAINS", WHITELISTED_DOMAINS)
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
}