package god.allah.api.auth

enum class AuthService(val authServer: String, val sessionServer: String) {
    MOJANG("https://authserver.mojang.com/", "https://sessionserver.mojang.com/"),
    ALTENING("http://authserver.thealtening.com/", "http://sessionserver.thealtening.com/");
}