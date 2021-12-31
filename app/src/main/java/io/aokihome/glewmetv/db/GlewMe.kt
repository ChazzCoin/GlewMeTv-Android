package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.getSafeString
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONObject

open class GlewMe : RealmObject() {

    @PrimaryKey
    var keyName: String = ""
    var name: String = ""
    var website: String = ""
    var youtube: String = ""
    var opensea: String = ""
    var discord: String = ""
    var reddit: String = ""
    var twitter: String = ""
    var description: String = ""
    var intro: String = ""

}

fun JSONObject.toGlewMe(key:String) : GlewMe {
    val glewMe = GlewMe()
    glewMe.keyName = key
    glewMe.name = getSafeString("name")
    glewMe.website = getSafeString("website")
    glewMe.youtube = getSafeString("youtube")
    glewMe.opensea = getSafeString("opensea")
    glewMe.discord = getSafeString("discord")
    glewMe.reddit = getSafeString("reddit")
    glewMe.twitter = getSafeString("twitter")
    glewMe.description = getSafeString("description")
    glewMe.intro = getSafeString("intro")
    return glewMe
}