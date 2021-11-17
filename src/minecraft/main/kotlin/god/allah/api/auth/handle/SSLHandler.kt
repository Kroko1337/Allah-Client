package god.allah.api.auth.handle

import god.allah.api.auth.AuthService
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

fun updateCertificateValidation(service: AuthService) {
    HttpsURLConnection.setDefaultSSLSocketFactory(service.sslSocketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier { hostname: String, session: SSLSession? -> hostname == service.authServer || hostname == service.sessionServer }
}

val allTrust = arrayOf<TrustManager>(object : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return null
    }

    override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
    override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
})

val allTrustFactory: SSLContext = SSLContext.getInstance("SSL")