package io.aokihome.glewmetv.utils

import com.github.kittinunf.fuel.core.Response
import io.aokihome.glewmetv.db.*
import org.json.JSONArray
import org.json.JSONObject

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

    class Metaverse(response: Response, save:Boolean=true, type:String=METAVERSE_INFO)
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
        const val GLEWME = "glewmetv"
        const val EVENTS = "events"
        const val HOOKUPS = "hookups"
    }

    //-> Initial Objects
    lateinit var masterResponse: Response
    var jsonObject: JSONObject?=null
    //-> Hookups
    var HookupList = mutableListOf<Hookup>()
    //-> Data Packages
    var MetaverseList = mutableListOf<io.aokihome.glewmetv.db.Metaverse>()
    var GlewMeList = mutableListOf<io.aokihome.glewmetv.db.GlewMe>()

    /** All Data Packages **/
    private fun initDataPackages(response: Response) {
        // GlewMe
        if (type == ALL_DATA_PACKAGES) {
            packageParser(GLEWME, response)
            packageParser(METAVERSE_INFO, response)
            packageParser(EVENTS, response)
            return
        }
        // Else:
        packageParser(type, response)
    }

    /** Runs Parser Based on Type Inputted **/
    private fun JSONObject.completePackage(type:String, key: String) {
        when (type) {
            GLEWME -> glewme(key, this)
            METAVERSE_INFO -> metaverse(key, this)
            EVENTS -> events(key, this)
        }
    }

    /** GLEWME Package **/
    private fun glewme(key: String, jsonObject: JSONObject?) {
        val glewMeObj = jsonObject?.toGlewMe(key)
        GlewMeList.add(glewMeObj ?: return)
        if (save) addGlewMeToSessionOnMain(glewMeObj)
    }

    /** METAVERSE INFO Package **/
    private fun metaverse(key: String, jsonObject: JSONObject?) {
        val metaObj = jsonObject?.toMetaverse(key)
        MetaverseList.add(metaObj ?: return)
        if (save) addMetaverseToSessionOnMain(metaObj)
    }

    /** Events Package **/
    private fun events(key: String, jsonObject: JSONObject?) {
        println("coming soon... $key, $jsonObject")
    }

    // Dynamic Package Parser
    private fun packageParser(type: String, response: Response) {
        jsonObject = response.toJsonObject()
        //-> Check if METAVERSE INFO exists.
        jsonObject?.get(type)?.let {
            val tempObj = it as? JSONObject
            tempObj?.let {
                for (key in it.keys()) {
                    //parse
                    val temp_obj = it.get(key) as? JSONObject
                    temp_obj?.completePackage(type, key)
                }
            }
        }
    }

    /** HOOKUPS **/
    private fun initHookupParser(response: Response) {
        masterResponse = response
        val hookupList = toListOfJsonObjects(response)
        for (item in hookupList) {
            val tempHookup = item.toHookup()
            HookupList.add(tempHookup)
            if (save) addHookupToSessionOnMain(tempHookup)
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


fun JSONObject.getSafeString(obj: String) : String {
    if (this.has(obj)) {
        return this.getString(obj)
    }
    return ""
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