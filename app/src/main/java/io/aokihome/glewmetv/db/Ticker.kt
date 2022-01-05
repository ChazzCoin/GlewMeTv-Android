package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.getDeepKey
import io.aokihome.glewmetv.utils.getSafeBoolean
import io.aokihome.glewmetv.utils.getSafeDouble
import io.aokihome.glewmetv.utils.getSafeString
import io.realm.RealmObject
import org.json.JSONObject
import kotlin.math.round

open class Ticker: RealmObject() {

    var name: String = ""
    var price: Double = 0.0
    var percentage_change_24h: Double = 0.0
    var isPos: Boolean = false

}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()
fun JSONObject.toTicker() : Ticker {
    val ticker = Ticker()
    ticker.apply {
        this.name = getSafeString("name")
        val temp = getDeepKey("USD") as? JSONObject
        val tempPrice = temp?.getDeepKey("price") as Double
        this.price = tempPrice.round()
        this.percentage_change_24h = temp.getSafeDouble("percent_change_24h")
        if (this.percentage_change_24h > 0.0) this.isPos = true

    }
    return ticker
}