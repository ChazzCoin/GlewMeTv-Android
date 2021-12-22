//package io.aokihome.tifbo.utils
//
//import android.app.Activity
//import android.content.Context
//import android.widget.Toast
//import com.github.kittinunf.fuel.core.Response
//import com.google.gson.Gson
//import io.aokihome.ript.aoki.rsKeyEncryption
//import io.aokihome.ript.ui.chat.Message
//import kotlinx.coroutines.*
//import org.json.JSONException
//import org.json.JSONObject
//import java.util.*
//
//class Utils {
//    val nothing = ""
//}
//
//
//
//fun CoroutineScope.toast(context: Context, message: String) {
//    this.launch(Dispatchers.Main) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//    }
//}
//
//fun showToastAsync(context: Context, message: String) {
//    GlobalScope.launch(Dispatchers.Main) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//    }
//}
//
///* -> !! EXTENSIONS !! <- */
//
//fun String.encrypt() : String {
//    return rsKeyEncryption().encryptStringAndSerialize(this)
//}
//
//fun String.decrypt() : String {
//    return rsKeyEncryption().decryptSerialized(this)
//}
//
//fun JSONObject.toMessage() : Message? {
//    if (this.has("message")) {
//        var id = ""
//        if (this.has("id")) {
//            id = this.getString("id") ?: ""
//        }
//        val username = this.getString("username") ?: ""
//        val to = this.getString("to") ?: ""
//        val room = this.getString("room") ?: ""
//        val message = this.getString("message") ?: "<unknown text>"
//        val isEncrypted = this.getBoolean("isEncrypted")
//        return Message().new(id = id,
//            username = username, to = to, room = room,
//            message = message, date = Date(), isEncrypted = isEncrypted, isLeft = true)
//    }
//    return null
//}
//
//fun String.parseForRipt() : JSONObject? {
//    try {
//        val jsonObject = JSONObject(this.replace("[", "").replace("]",""))
//        if (jsonObject.has("nameValuePairs")) {
//            val resultObject = JSONObject(jsonObject.getString("nameValuePairs"))
//            return resultObject
//        }
//    } catch (e: JSONException) {
//        return null
//    }
//    return null
//}
//
//fun Response.parseLogs() : JSONObject? {
//    val body = String(this.body().toByteArray(), Charsets.UTF_8)
//    try {
//        val gBody = Gson().toJson(body)
//        val convertByteArrays = gBody /* b' -> " */
//            .replace("b\\u0027", "\"")
//        val singleQuotesToDouble = convertByteArrays /* ' -> " */
//            .replace("\\u0027", "\"")
//        val removeFirstCharacter = StringBuilder(singleQuotesToDouble).deleteCharAt(0)
//        val removeLastCharacter = removeFirstCharacter.deleteCharAt(removeFirstCharacter.lastIndex)
//        return JSONObject(removeLastCharacter.toString())
//    } catch (e: JSONException) {
//        println("RESPONSE.TOJSON ERROR: $e")
//    }
//    return null
//}
//
//fun Response.parseToUser() : JSONObject? {
//    val body = String(this.body().toByteArray(), Charsets.UTF_8)
//    try {
//        val gBody = Gson().toJson(body)
//        val convertByteArrays = gBody /* b' -> " */
//            .replace("b\\u0027", "\"")
//        val singleQuotesToDouble = convertByteArrays /* ' -> " */
//            .replace("\\u0027", "\"")
//        val removeFirstCharacter = StringBuilder(singleQuotesToDouble).deleteCharAt(0)
//        val removeLastCharacter = removeFirstCharacter.deleteCharAt(removeFirstCharacter.lastIndex)
//        return JSONObject(removeLastCharacter.toString())
//    } catch (e: JSONException) {
//        println("RESPONSE.TOJSON ERROR: $e")
//    }
//    return null
//}
//
//fun Response.parseToList() : List<String>? {
//    val body = String(this.body().toByteArray(), Charsets.UTF_8)
//    val re = ",(?=([\"]*\"[^\"]*\")*[^\"])".toRegex()
//    try {
//        val gBody = Gson().toJson(body)
//        val obj = gBody.replace("\\u0027", "\"")
//        val removeFirstCharacter = StringBuilder(obj).deleteCharAt(0)
//        val removeLastCharacter = removeFirstCharacter.deleteCharAt(removeFirstCharacter.lastIndex)
//        val str = removeLastCharacter.toString().replace("[", "").replace("]", "")
//        return loopList(str.split(re))
//    } catch (e: JSONException) {
//        println("RESPONSE.TOJSON ERROR: $e")
//    }
//    return null
//}
//
//fun loopList(l:List<String>): MutableList<String> {
//    val newList = mutableListOf<String>()
//    for (i in l) {
//        val removeSpaces = i.replace(" ", "")
//        val removeQuotes = removeSpaces.replace("\"", "")
//        newList.add(removeQuotes)
//    }
//    return newList
//}
//
//
///* SAFE way to CHECK and GET key value in json  */
//fun JSONObject.riptString(key:String) : String {
//    return if (this.has(key)) { this.getString(key) } else { "" }
//}
//
//fun JSONObject.riptBoolean(key:String) : Boolean {
//    return if (this.has(key)) { this.getBoolean(key) } else { true }
//}
//
///* -> Run the BLOCK if ALL variables are NOT NULL.
//* https://stackoverflow.com/questions/35513636/multiple-variable-let-in-kotlin */
//inline fun <T: Any> letMany(vararg elements: T?, closure: (List<T>) -> Unit) {
//    if (elements.all { it != null }) {
//        closure(elements.filterNotNull())
//    }
//}
//
///*
//-> A Swift Design alternative/opposite to 'ifLet' to protect against NULL
//-> Run the BLOCK only if ALL variables are NULL
//-> 'Guard' against allowing the code to continue beyond this block.
//        Often used with a 'return' statement inside the block itself to bail out completely.
//*/
//inline fun <T: Any> guardMany(vararg elements: T?, closure: () -> Nothing): List<T> {
//    return if (elements.all { it != null }) {
//        elements.filterNotNull()
//    } else {
//        closure()
//    }
//}
