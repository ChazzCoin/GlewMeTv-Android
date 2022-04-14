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
        val URL_BASE_one = "http://192.168.1.240:3671"
        val URL_BASE = "http://192.168.1.42:3671"
        val URL_ARTICLES_DATA = "$URL_BASE/articles"
        val URL_SEARCH_ARTICLES_DATA = "$URL_BASE/articles/search/"
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

    fun searchAsync(searchTerm: String) = riptIODispatcher.async {
        search(searchTerm)
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

    private suspend fun search(searchTerm:String) : Response = withContext(riptIODispatcher.coroutineContext) {
        val httpAsync = "${URL_SEARCH_ARTICLES_DATA}${searchTerm}".httpGet().responseString { request, response, result ->
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














