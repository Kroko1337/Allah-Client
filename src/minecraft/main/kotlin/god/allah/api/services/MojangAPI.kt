package god.allah.api.services

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import god.allah.api.utils.decode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

private const val servicesApi = "https://api.minecraftservices.com"
private const val mojangApi = "https://api.mojang.com"
private const val sessionApi = "https://sessionserver.mojang.com"

fun getUUID(name: String) : String {
    val url = URL("$mojangApi/users/profiles/minecraft/$name")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for(line in reader.readLines())
        builder.append(line).append("\n")
    val parser = JsonParser()
    val json = parser.parse(builder.toString()) as JsonObject
    val uuid = json.get("id").asString
    connection.disconnect()
    return uuid
}

fun getSkin(uuid: String) : String {
    val url = URL("$sessionApi/session/minecraft/profile/$uuid")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for(line in reader.readLines())
        builder.append(line).append("\n")
    val parser = JsonParser()
    val json = parser.parse(builder.toString()) as JsonObject
    val array = json.getAsJsonArray("properties")
    val properties = array.get(0).asJsonObject
    val encoded = properties.get("value").asString
    val propertiesObject = parser.parse(decode(encoded)) as JsonObject
    val textures = propertiesObject.getAsJsonObject("textures")
    val skin = textures.getAsJsonObject("SKIN")
    val skinUrl = skin.get("url").asString
    connection.disconnect()
    return skinUrl
}

fun changeName(name: String, token: String) : Response {
    val url = URL("$servicesApi/minecraft/profile/name/$name")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "PUT"
    connection.doOutput = true
    connection.setRequestProperty("Authorization", "Bearer $token")
    connection.setRequestProperty("Content-Type", "application/json")
    val responseCode = connection.responseCode
    val responseMessage = connection.responseMessage
    connection.disconnect()
    return Response(responseCode, responseMessage)
}

fun changeSkin(variant: SkinVariant, skinUrl: String, token: String) : Response {
    val url = URL("$servicesApi/minecraft/profile/skins")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "POST"
    connection.doOutput = true
    connection.doInput = true
    connection.setRequestProperty("Authorization", "Bearer $token")
    connection.setRequestProperty("Content-Type", "application/json")

    val jsonObject = JsonObject()
    jsonObject.addProperty("variant", variant.variant)
    jsonObject.addProperty("url", skinUrl)

    val json = jsonObject.toString()
    val out = json.toByteArray(StandardCharsets.UTF_8)
    val outputStream = connection.outputStream
    outputStream.write(out)
    val responseCode = connection.responseCode
    val responseMessage = connection.responseMessage
    connection.disconnect()
    return Response(responseCode, responseMessage)
}

enum class SkinVariant(val variant: String) {
    CLASSIC("classic"), SLIM("slim")
}