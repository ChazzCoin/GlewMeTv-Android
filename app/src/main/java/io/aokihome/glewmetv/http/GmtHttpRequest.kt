package io.aokihome.glewmetv.http

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class GmtHttpRequest {
    constructor()

    companion object {
        val URL_BASE = "http://192.168.1.100:3671"
        val URL_HOOKUPS_DATA = "$URL_BASE/hookups"
        val URL_GLEWMETV_DATA = "$URL_BASE/glewmetv/data"
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














