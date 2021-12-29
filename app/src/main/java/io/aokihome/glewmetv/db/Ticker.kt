package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.getSafeBoolean
import io.aokihome.glewmetv.utils.getSafeString
import io.realm.RealmObject
import org.json.JSONObject

open class Ticker: RealmObject() {

    var name: String = ""
    var price: String = ""
    var isPos: Boolean = false

}

fun JSONObject.parseToTicker() : Ticker {
    val ticker = Ticker()
    ticker.apply {
        this.name = getSafeString("name")
        this.price = getSafeString("price")
        this.isPos = getSafeBoolean("isPos")

    }
    return ticker
}