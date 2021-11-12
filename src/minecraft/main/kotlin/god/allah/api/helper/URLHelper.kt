package god.allah.api.helper

import java.net.URI
import java.net.URL

object URLHelper {
    fun getYouTubeThumbnail(link: String) : String {
        val url = URL(link)
        val uri = URI(url.protocol, url.userInfo, url.host, url.port, url.path, url.query, url.ref)
        val replace = uri.query.replace("v=","")
        return "https://img.youtube.com/vi/$replace/0.jpg"
    }
}