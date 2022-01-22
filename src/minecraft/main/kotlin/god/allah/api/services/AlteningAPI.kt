package god.allah.api.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import god.allah.api.auth.account.AlteningAccount
import god.allah.api.auth.account.AlteningLicense
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val api = "https://api.thealtening.com/v2"
private val parser = JsonParser()

fun generateAlt(apiKey: String): AlteningAccount {
    val url = URL("$api/generate?key=$apiKey")
    println(url.toString())
    val connection : HttpsURLConnection = url.openConnection() as HttpsURLConnection
    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
    connection.requestMethod = "GET"
    connection.doInput = true
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for (line in reader.readLines())
        builder.append(line).append("\n")
    val json = parser.parse(builder.toString()) as JsonObject
    val token = json.get("token").asString
    val userName = json.get("username").asString
    val limit = json.get("username").asBoolean
    val infos = getInfos(json)
    connection.disconnect()
    return AlteningAccount(userName, token, limit, infos)
}

fun addToPrivate(apiKey: String, token: String): Boolean {
    val url = URL("$api/private?key=$apiKey&token=$token")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for (line in reader.readLines())
        builder.append(line).append("\n")
    val json = parser.parse(builder.toString()) as JsonObject
    val success = json.get("success").asBoolean
    connection.disconnect()
    return success
}

fun getPrivates(apiKey: String): ArrayList<AlteningAccount> {
    val url = URL("$api/privates?key=$apiKey")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for (line in reader.readLines())
        builder.append(line).append("\n")
    val json = parser.parse(builder.toString()) as JsonArray
    val list = ArrayList<AlteningAccount>()
    json.forEach {
        when (it) {
            is JsonObject -> {
                val obj = it.asJsonObject
                list.add(
                    AlteningAccount(
                        obj.get("username").asString,
                        obj.get("token").asString,
                        obj.get("limit").asBoolean,
                        getInfos(obj)
                    )
                )
            }
        }
    }
    connection.disconnect()
    return list
}

fun addToFavorite(apiKey: String, token: String): Boolean {
    val url = URL("$api/favorite?key=$apiKey&token=$token")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for (line in reader.readLines())
        builder.append(line).append("\n")
    val json = parser.parse(builder.toString()) as JsonObject
    val success = json.get("success").asBoolean
    connection.disconnect()
    return success
}

fun getFavorites(apiKey: String): ArrayList<AlteningAccount> {
    val url = URL("$api/favorites?key=$apiKey")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for (line in reader.readLines())
        builder.append(line).append("\n")
    val json = parser.parse(builder.toString()) as JsonArray
    val list = ArrayList<AlteningAccount>()
    json.forEach {
        when (it) {
            is JsonObject -> {
                val obj = it.asJsonObject
                list.add(
                    AlteningAccount(
                        obj.get("username").asString,
                        obj.get("token").asString,
                        obj.get("limit").asBoolean,
                        getInfos(obj)
                    )
                )
            }
        }
    }
    connection.disconnect()
    return list
}

fun getLicense(apiKey: String): AlteningLicense {
    val url = URL("$api/license?key=$apiKey")
    val connection = url.openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/json")
    val inputStream = connection.inputStream
    val reader = BufferedReader(InputStreamReader(inputStream))
    val builder = StringBuilder()
    for (line in reader.readLines())
        builder.append(line).append("\n")
    val json = parser.parse(builder.toString()) as JsonObject
    val username = json.get("username").asString
    val hasLicense = json.get("hasLicense").asBoolean
    val licenseType = json.get("licenseType").asString
    val expires = json.get("expires").asString
    connection.disconnect()
    return AlteningLicense(username, hasLicense, licenseType, expires)
}

private fun getInfos(json: JsonObject) : HashMap<String, String> {
    val infos =  HashMap<String, String>()
    if (json.has("info") && !json.get("info").isJsonNull) {
        val info = json.get("info").asJsonObject
        if (info.has("hypixel.lvl"))
            infos["Hypixel Level"] = info.get("hypixel.lvl").asString
        if (info.has("hypixel.rank"))
            infos["Hypixel Rank"] = info.get("hypixel.rank").asString
        if (info.has("mineplex.lvl"))
            infos["Mineplex Level"] = info.get("mineplex.lvl").asString
        if (info.has("mineplex.rank"))
            infos["Mineplex Rank"] = info.get("mineplex.rank").asString
        if (info.has("labymod.cape"))
            infos["LabyMod Cape"] = info.get("labymod.cape").asString
        if (info.has("5zig.cape"))
            infos["5zig Cape"] = info.get("5zig.cape").asString
    }
    return infos
}
