package god.allah.api.utils

import com.mojang.authlib.Agent
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import god.allah.api.Wrapper
import god.allah.api.Wrapper.mc
import god.allah.api.auth.AuthService
import god.allah.api.auth.handle.switchTo
import god.allah.api.services.MicrosoftToken
import god.allah.api.services.generateAlt
import god.allah.api.services.getUUID
import god.allah.api.services.logIn
import net.minecraft.util.Session
import java.net.Proxy

var status = "Waiting..."

fun generateAltening(apiKey: String) {
    switchTo(AuthService.ALTENING)
    Thread() {
        run {
            status = "Generating..."
            val account = generateAlt(apiKey)
            val service = YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT)
            service.setUsername(account.token)
            service.setPassword(Wrapper.name)

            service.logIn()
            status = "Logged into §e${service.selectedProfile.name}"
            mc.session = Session(service.selectedProfile.name, service.selectedProfile.id.toString(), service.authenticatedToken, "LEGACY")
        }
    }.start()
}

fun loginAltening(token: String) {
    if(token.contains("@alt")) {
        switchTo(AuthService.ALTENING)
        Thread() {
            run {
                status = "Logging in..."
                val service = YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT)
                service.setUsername(token)
                service.setPassword(Wrapper.name)

                service.logIn()
                status = "Logged into §e${service.selectedProfile.name}"
                mc.session = Session(service.selectedProfile.name, service.selectedProfile.id.toString(), service.authenticatedToken, "LEGACY")
            }
        }.start()
    }
}

fun loginMojang(email: String, password: String) {
    switchTo(AuthService.MOJANG)
    Thread() {
        run {
            status = "Logging in..."
            val service = YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT)
            service.setUsername(email)
            service.setPassword(password)
            service.logIn()
            status = "Logged into §e${service.selectedProfile.name}"
            mc.session = Session(service.selectedProfile.name, service.selectedProfile.id.toString(), service.authenticatedToken, "LEGACY")
        }
    }.start()
}

fun loginSession(session: String, name: String) { //Not working atm
    switchTo(AuthService.MOJANG)
    Thread() {
        run {
            status = "Logging in..."
            val uuid = getUUID(name)
            mc.session = Session(name, uuid, session, "LEGACY")
            status = "Logged into §e$name"
        }
    }.start()
}

fun loginMicrosoft(email: String, password: String) {
    status = "Logging in..."
    val session = logIn(email, password)
    status = "Logged into ${session.username}"
}