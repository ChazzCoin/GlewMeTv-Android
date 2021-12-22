package io.aokihome.glewmetv.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class Hookup(

    var author: String,
    var title: String,
    var description: String,
    var body: String,
    var published_date: String,
    var url: String,
    var imgUrl: String,
    var source: String?,
    var source_rank: String?,
    var category: String,
    var sentiment: String?,
    var score: Double,
    var title_score: Double,
    var description_score: Double,
    var body_score: Double,
    var rank: Double,

) : Parcelable

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

fun JSONObject.getSafeJsonObj(obj: String) : JSONObject? {
    if (this.has(obj)) {
        return this.getJSONObject(obj)
    }
    return null
}

fun JSONObject.parseToHookup() : Hookup {
    val hookup = Hookup(
        author = this@parseToHookup.getSafeString("author"),
        title = this@parseToHookup.getSafeString("title"),
        description = this@parseToHookup.getSafeString("description"),
        body = this@parseToHookup.getSafeString("body"),
        published_date = this@parseToHookup.getSafeString("published_date"),
        url = this@parseToHookup.getSafeString("url"),
        imgUrl = this@parseToHookup.getSafeString("img_url"),
        source = this@parseToHookup.getSafeString("source"),
        source_rank = this@parseToHookup.getSafeString("source_rank"),
        category = this@parseToHookup.getSafeString("category"),
        sentiment = this@parseToHookup.getSafeString("sentiment"),
        score = this@parseToHookup.getSafeDouble("score"),
        title_score = this@parseToHookup.getSafeDouble("title_score"),
        description_score = this@parseToHookup.getSafeDouble("description_score"),
        body_score = this@parseToHookup.getSafeDouble("body_score"),
        rank = this@parseToHookup.getSafeDouble("rank")
    )
    return hookup
}