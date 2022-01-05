package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.*
import io.realm.RealmObject
import org.json.JSONObject

open class Event : RealmObject() {

    var id: String = ""
    var live: Boolean = false
    var trending: Boolean = false
    var highlighted: Boolean = false
    var approved: Boolean = false
    var name: String = ""
    var image: String = ""
    var description: String = ""
    var start_at: String = ""
    var finish_at: String = ""
    var user: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    var total_attendees: Int = 0
    var latest_attendees: String = ""
    var url: String = ""
    var recurrent_dates: String = ""
    var estate_id: String = ""
    var estate_name: String = ""
    var x: String = ""
    var y: String = ""

}

fun JSONObject.toEvent() : Event {
    val event = Event()
    event.apply {
        this.id = getSafeString("id")
        this.live = getSafeBoolean("live")
        this.name = getSafeString("name")
        this.image = getSafeString("image")
        this.description = getSafeString("description")
        this.start_at = getSafeString("start_at")
        this.finish_at = getSafeString("finish_at")
        this.user = getSafeString("user")
        this.approved = getSafeBoolean("approved")
        this.created_at = getSafeString("created_at")
        this.updated_at = getSafeString("updated_at")
        this.total_attendees = getSafeInt("total_attendees")
        this.latest_attendees = getSafeString("latest_attendees")
        this.url = getSafeString("url")
        this.recurrent_dates = getSafeString("recurrent_dates")
        this.trending = getSafeBoolean("trending")
        this.estate_id = getSafeString("estate_id")
        this.estate_name = getSafeString("estate_name")
        this.x = getSafeString("x")
        this.y = getSafeString("y")
        this.highlighted = getSafeBoolean("highlighted")

    }
    return event
}