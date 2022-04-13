package io.aokihome.glewmetv.utils

import com.github.kittinunf.fuel.core.Response
import io.aokihome.glewmetv.db.Hookup
import io.aokihome.glewmetv.db.addHookupToSessionOnMain
import io.aokihome.glewmetv.db.toHookup
import org.json.JSONArray
import org.json.JSONObject

var articleList = mutableListOf<Hookup>()

/** HOOKUPS **/
fun ArticleParser(response: Response): MutableList<Hookup> {
    val hookupList = toListOfJsonObjects(response)
    for (item in hookupList) {
        val tempHookup = item.toHookup()
        articleList.add(tempHookup)
        addHookupToSessionOnMain(tempHookup)
    }
    return articleList
}

private fun toListOfJsonObjects(response: Response) : List<JSONObject> {
    val tempList = mutableListOf<JSONObject>()
    try {
        val body = String(response.body().toByteArray(), Charsets.UTF_8)
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

private fun toListOfJsonObjects(jsonArray: JSONArray) : List<JSONObject> {
    val tempList = mutableListOf<JSONObject>()
    try {
        val condition = true
        var i = 0
        while (condition) {
            if (jsonArray[i] != null) {
                val current_obj = jsonArray[i] as? JSONObject
                i++
                tempList.add(current_obj ?: continue)
            } else {
                break
            }
        }
    } catch (e: java.lang.Exception) {
        println("Couldn't parse object.")
    }
    return tempList
}

