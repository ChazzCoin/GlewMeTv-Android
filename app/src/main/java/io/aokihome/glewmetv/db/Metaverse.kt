package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.getSafeDouble
import io.aokihome.glewmetv.utils.getSafeInt
import io.aokihome.glewmetv.utils.getSafeString
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONObject

open class Metaverse : RealmObject() {

    @PrimaryKey
    var metaverseName: String = ""
    var id: Int = 0
    var category: String = ""
    var contract_address: String? = null
    var date_added: String = ""
    var date_launched: String = ""
    var description: String = ""
    var is_hidden: Int = 0
    var logo: String = ""
    var name: String = ""
    var notice: String = ""
    var platform: String? = null
    var self_reported_circulating_supply: String = ""
    var self_reported_tags: String = ""
    var slug: String = ""
    var subreddit: String = ""
    var symbol: String = ""
    var tagGroups: RealmList<String>? = null
    var tagNames: RealmList<String>? = null
    var tags: RealmList<String>? = null
    var twitter_username: String = ""
    var urls: String? = null
}
fun JSONObject.toMetaverse(key: String) : Metaverse {
    val metaverse = Metaverse()
    metaverse.metaverseName = key
    metaverse.id = getSafeInt("id")
    metaverse.category = getSafeString("category")
    metaverse.contract_address = getSafeString("contract_address")
    metaverse.date_added = getSafeString("date_added")
    metaverse.description = getSafeString("description")
    metaverse.date_launched = getSafeString("date_launched")
    metaverse.description = getSafeString("description")
    metaverse.is_hidden = getSafeInt("is_hidden")
    metaverse.logo = getSafeString("logo")
    metaverse.name = getSafeString("name")
    metaverse.notice = getSafeString("notice")
    metaverse.platform = getSafeString("platform")
    metaverse.self_reported_circulating_supply = getSafeString("self_reported_circulating_supply")
    metaverse.self_reported_tags = getSafeString("self_reported_tags")
    metaverse.slug = getSafeString("slug")
    metaverse.subreddit = getSafeString("subreddit")
    metaverse.symbol = getSafeString("symbol")
    metaverse.tagGroups = null
    metaverse.tagNames = null
    metaverse.tags = null
    metaverse.twitter_username = getSafeString("twitter_username")
    metaverse.urls = getSafeString("urls")
    return metaverse
}
