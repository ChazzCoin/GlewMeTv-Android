package io.aokihome.glewmetv.http

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class RiptHttpRequest {
    constructor()

    companion object {
        val VAATU_BASE_URL = "http://192.168.1.100:3671/hookups"

        fun parseHookups(obj: Response) : List<JSONObject> {
            val tempList = mutableListOf<JSONObject>()
            try {
                val body = String(obj.body().toByteArray(), Charsets.UTF_8)
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
    }

    private val job = SupervisorJob()
    private val riptIODispatcher = CoroutineScope(Dispatchers.IO + job)
    private val riptMainDispatcher = CoroutineScope(Dispatchers.Main + job)

    /**
     * PUBLIC CONVENIENCE METHODS
     */
    fun getAsync(url: String) = riptIODispatcher.async {
        get(url)
    }

    /**
     * PRIVATE BASE CALLS
     */
    private suspend fun get(url:String) : Response = withContext(riptIODispatcher.coroutineContext) {
        val httpAsync = url.httpGet().responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    println("Failed: ${result.error}")
                }
                is Result.Success -> {
                    println("Success: ${result.get()}")
                }
            }
        }
        return@withContext httpAsync.response().second
    }

}