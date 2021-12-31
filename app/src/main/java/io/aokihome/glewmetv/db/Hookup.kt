package io.aokihome.glewmetv.db

import com.github.kittinunf.fuel.core.Response
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.utils.getSafeDouble
import io.aokihome.glewmetv.utils.getSafeString
import io.realm.RealmObject
import org.json.JSONArray
import org.json.JSONObject

open class Hookup : RealmObject() {

    var id: String = ""
    var author: String = ""
    var title: String = ""
    var description: String = ""
    var body: String = ""
    var published_date: String? = ""
    var url: String = ""
    var imgUrl: String = ""
    var source: String? = ""
    var source_rank: String? = ""
    var category: String = ""
    var sentiment: String? = ""
    var score: Double = 0.0
    var title_score: Double = 0.0
    var description_score: Double = 0.0
    var body_score: Double = 0.0
    var rank: Double = 0.0
}

fun MutableList<Hookup>.filterOutSource(source:String) {
    this.filter { it.source.toString().contains(source) }
}

fun Response.toJsonObject() : JSONObject? {
    try {
        val body = String(this.body().toByteArray(), Charsets.UTF_8)
        return JSONObject(body)
    } catch (e: java.lang.Exception) {
        println("Couldn't parse object.")
    }
    return null
}

fun JSONArray.toListOfJsonObjects() : List<JSONObject> {
    val tempList = mutableListOf<JSONObject>()
    try {
        val condition = true
        var i = 0
        while (condition) {
            if (this[i] != null) {
                val current_obj = this[i]
                val jsonObject = JSONObject(current_obj.toString())
                tempList.add(jsonObject)
                i++
            } else {
                break
            }
        }
    } catch (e: java.lang.Exception) {
        println("Couldn't parse object.")
    }
    return tempList
}

fun Response.toListOfJsonObjects() : List<JSONObject> {
    val tempList = mutableListOf<JSONObject>()
    try {
        val body = String(this.body().toByteArray(), Charsets.UTF_8)
        val jsonArray = JSONArray(body)
        val condition = true
        var i = 0
        while (condition) {
            if (jsonArray[i] != null) {
                val current_obj = jsonArray[i]
                val jsonObject = JSONObject(current_obj.toString())
                tempList.add(jsonObject)
                i++
            } else {
                break
            }
        }
    } catch (e: java.lang.Exception) {
        println("Couldn't parse object.")
    }
    return tempList
}

fun JSONObject.toHookup() : Hookup {
    val hookup = Hookup()
    hookup.id = getSafeString("id")
    hookup.author = getSafeString("author")
    hookup.author = getSafeString("author")
    hookup.title = getSafeString("title")
    hookup.description = getSafeString("description")
    hookup.body = getSafeString("body").trim()
    hookup.published_date = getSafeString("published_date")
    hookup.url = getSafeString("url")
    hookup.imgUrl = getSafeString("img_url")
    hookup.source = getSafeString("source")
    hookup.source_rank = getSafeString("source_rank")
    hookup.category = getSafeString("category")
    hookup.sentiment = getSafeString("sentiment")
    hookup.score = getSafeDouble("score")
    hookup.title_score = getSafeDouble("title_score")
    hookup.description_score = getSafeDouble("description_score")
    hookup.body_score = getSafeDouble("body_score")
    hookup.rank = getSafeDouble("rank")
    return hookup
}
