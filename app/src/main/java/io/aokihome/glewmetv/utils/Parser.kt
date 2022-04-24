package io.aokihome.glewmetv.utils

import com.github.kittinunf.fuel.core.Response
import io.aokihome.glewmetv.db.*
import org.json.JSONArray
import org.json.JSONObject
import com.google.gson.Gson
import io.realm.RealmObject

sealed class Parser(val save:Boolean=true, val type:String) : JSONObject() {

    /** HOOKUPS
     * -> toHookup() located in db.Hookup class
     * **/
    class Hookups(response: Response, save:Boolean=true, type:String=HOOKUPS)
        : Parser(save, type, response)

    /** DATA PACKAGES
     * -> Each "toClass()" helper method is held inside db.Class
     * **/
    class AllDataPackages(response: Response, save:Boolean=true, type:String= ALL_DATA_PACKAGES)
        : Parser(save, type, response)

    class Metaverse_INFO(response: Response, save:Boolean=true, type:String=METAVERSE_INFO)
        : Parser(save, type, response)

    class Metaverse_PRICES(response: Response, save:Boolean=true, type:String= METAVERSE_PRICE_INFO)
        : Parser(save, type, response)

    class GlewMe(response: Response, save:Boolean=true, type:String=GLEWME)
        : Parser(save, type, response)

    class Events(response: Response, save:Boolean=true, type:String= EVENTS)
        : Parser(save, type, response)

    /** Primary Constructor **/
    constructor(save:Boolean=true, type:String, response: Response) : this(save, type) {
        when (type) {
            HOOKUPS -> initHookupParser(response)
            else -> initDataPackages(response)
        }
    }

    companion object {
        const val ALL_DATA_PACKAGES = "all_data_packages"
        const val METAVERSE_INFO = "meta_ticker_info"
        const val METAVERSE_PRICE_INFO = "meta_price_info"
        const val GLEWME = "glewmetv"
        const val EVENTS = "events"
        const val HOOKUPS = "hookups"
    }

    //-> Initial Objects
    lateinit var masterResponse: Response
    var jsonObject: JSONObject?=null
    //-> Hookups
    var HookupList = mutableListOf<Article>()
    //-> Data Packages
    var metaJsonList = mutableListOf<JSONObject>()
    var MetaverseList = mutableListOf<Metaverse>()
    var PriceList = mutableListOf<Ticker>()
    var GlewMeList = mutableListOf<io.aokihome.glewmetv.db.GlewMe>()
    var EventList = mutableListOf<Event>()

    /** All Data Packages **/
    private fun initDataPackages(response: Response) {
        // GlewMe
        jsonObject = response.toJsonObject()
        if (type == ALL_DATA_PACKAGES) {
            packageParser(METAVERSE_PRICE_INFO)
            packageParser(GLEWME)
            packageParser(METAVERSE_INFO)
            packageParser(EVENTS)
            return
        }
        // Else:
        packageParser(type)
    }

    /** Runs Parser Based on Type Inputted **/
    private fun JSONObject.completePackage(type:String, key: String) {
        when (type) {
            GLEWME -> glewme(key, this)
            METAVERSE_INFO -> metaverse(key, this)
            METAVERSE_PRICE_INFO -> prices(this)
            EVENTS -> events(key, this)
        }
    }

    /** GLEWME Package **/
    private fun glewme(key: String, jsonObject: JSONObject?) {
        val glewMeObj = jsonObject?.toGlewMe(key)
        GlewMeList.add(glewMeObj ?: return)

    }

    /** METAVERSE INFO Package **/
    private fun metaverse(key: String, jsonObject: JSONObject?) {
        try {
            val metaObj = jsonObject?.toMetaverse(key)
            if (jsonObject != null) {
                metaJsonList.add(jsonObject)
            }
            MetaverseList.add(metaObj ?: return)
        } catch (e: Exception) {
            println("!!!!Failed Parser().Metaverse(): $e")
        }
    }

    /** METAVERSE PRICES Package **/
    private fun prices(jsonObject: JSONObject?) {
        val priceObj = jsonObject?.toTicker()
        PriceList.add(priceObj ?: return)
        println(PriceList)
    }

    /** Events Package **/
    private fun events(key: String, jsonObject: JSONObject?) {
        val eventObj = jsonObject?.toEvent()
        EventList.add(eventObj ?: return)
        println(eventObj)
    }

    // Dynamic Package Parser
    private fun packageParser(type: String) {
        try {
            //-> Check if METAVERSE INFO exists.
            jsonObject?.get(type)?.let {
                val tempObj = it as? JSONObject
                tempObj?.let {
                    for (key in it.keys()) {
                        //parse
                        val obj = it.get(key)
                        if (type == EVENTS) {
                            val temp = (obj as? JSONArray) ?: continue
                            val event_list = toListOfJsonObjects(jsonArray = temp)
                            for (item in event_list) {
                                item.completePackage(type, key)
                            }
                            continue
                        }
                        (obj as? JSONObject)?.completePackage(type, key)
                    }
                }
            }
        } catch (e: Exception) {
            println("!!!!Failed Parser().packageParser(): $e")
        }
    }

    /** HOOKUPS **/
    private fun initHookupParser(response: Response) {
        masterResponse = response
        val hookupList = toListOfJsonObjects(response)
        for (item in hookupList) {
            val tempHookup = item.toArticle()
            HookupList.add(tempHookup)
            if (save) addArticleToSessionOnMain(tempHookup)
        }
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

}

fun RealmObject.toJSON(): JSONObject? {
    tryCatch {
        val jsonString = Gson().toJson(this)
        return JSONObject(jsonString)
    }
    return null
}

/** JSON HELPER METHODS **/
// p = "coin" c = "symbol"
fun JSONObject.getParentChild(parentKey:String, childKey:String) : Any? {
    val parentTemp = this.getDeepKey(parentKey)
    if (parentTemp is JSONObject) {
        val childTemp = parentTemp.getDeepKey(childKey)
        return childTemp
    }
    return null
}

fun JSONObject.getDeepKey(key: String): Any? {
    if (this.has(key)) return this.get(key)
    for (tempKey in this.keys()) {
        val tempValue = this[tempKey]
        if (key == tempKey) return tempValue
        // -> If JSON Object
        if (tempValue is JSONObject) {
            val result = tempValue.getDeepKey(key)
            result?.let { return it }
        }
        //-> If JSON Array of Objects
        if (tempValue is JSONArray) {
            val condition = true
            var i = 0
            while (condition) {
                if (tempValue[i] != null) {
                    val current_obj = tempValue[i]
                    if (current_obj is JSONObject) {
                        val result = current_obj.getDeepKey(key)
                        result?.let { return it }
                    }
                    i++
                } else {
                    break
                }
            }
        }
    }
    return null
}


fun JSONObject.getSafeString(obj: String, default:String="") : String {
    if (this.has(obj)) {
        return this.getString(obj)
    }
    return default
}

fun JSONObject.getSafeDouble(obj: String) : Double {
    if (this.has(obj)) {
        return this.getDouble(obj)
    }
    return 0.0
}

fun JSONObject.getSafeInt(obj: String) : Int {
    if (this.has(obj)) {
        return this.getInt(obj)
    }
    return 0
}

fun JSONObject.getSafeBoolean(obj: String) : Boolean {
    if (this.has(obj)) {
        return this.getBoolean(obj)
    }
    return false
}

fun JSONObject.getSafeJsonObj(obj: String) : JSONObject? {
    if (this.has(obj)) {
        return this.getJSONObject(obj)
    }
    return null
}