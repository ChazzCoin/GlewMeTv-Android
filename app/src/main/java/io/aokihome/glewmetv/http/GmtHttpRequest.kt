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
//        val URL_BASE = "http://aokiromeo.duckdns.org:3671"
        val URL_BASE = "http://192.168.1.42:1763"
//        val URL_BASE = "http://192.168.1.166:3671"
//        val URL_BASE = "http://192.168.1.240:3671"
//        val URL_BASE = "http://192.168.1.42:3671"
        val URL_ARTICLES_DATA = "$URL_BASE/articles"
        val URL_SEARCH_ARTICLES_DATA = "$URL_BASE/articles/search/"
        val URL_DOWNLOAD_ARTICLE = "$URL_BASE/articles/download/"
        val URL_GLEWMETV_DATA = "$URL_BASE/glewmetv/data"
    }

    private val job = SupervisorJob()
    private val riptIODispatcher = CoroutineScope(Dispatchers.IO + job)

    /**
     * PUBLIC CONVENIENCE METHODS
     */
    fun getAsync(url: String) = riptIODispatcher.async {
        get(url)
    }

    fun getGlewMeTvData() = riptIODispatcher.async {
        get(URL_GLEWMETV_DATA)
    }

    fun searchAsync(searchTerm: String) = riptIODispatcher.async {
        search(searchTerm)
    }

    fun downloadUrlAsync(url: String) = riptIODispatcher.async {
        downloadUrl(url)
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

    private suspend fun downloadUrl(url:String) : Response = withContext(riptIODispatcher.coroutineContext) {
        val httpAsync = URL_DOWNLOAD_ARTICLE.httpGet(listOf("url" to url)).responseString { request, response, result ->
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














