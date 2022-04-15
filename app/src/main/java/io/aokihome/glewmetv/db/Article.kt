package io.aokihome.glewmetv.db

import android.app.Activity
import com.github.kittinunf.fuel.core.Response
import io.aokihome.glewmetv.ui.main.MainGlewMeTvActivity
import io.aokihome.glewmetv.ui.readArticleDialog
import io.aokihome.glewmetv.utils.getSafeDouble
import io.aokihome.glewmetv.utils.getSafeString
import io.realm.RealmObject
import org.json.JSONObject

open class Article : RealmObject() {

    var id: String = ""
    var author: String = ""
    var title: String = ""
    var description: String = ""
    var body: String = ""
    var published_date: String? = ""
    var url: String = ""
    var imgUrl: String = ""
    var source: String? = ""
    var source_rank: String? = ""
    var category: String = ""
    var sentiment: String? = ""
    var score: Double = 0.0
    var title_score: Double = 0.0
    var description_score: Double = 0.0
    var body_score: Double = 0.0
    var rank: Double = 0.0
}

fun Article.openArticle(activity: Activity?=MainGlewMeTvActivity.context) {
    readArticleDialog(activity ?: return, this).show()
}

fun MutableList<Article>.filterOutSource(source:String) {
    this.filter { it.source.toString().contains(source) }
}

fun MutableList<Article>?.search(searchTerm:String): MutableList<Article> {
    return this?.filter {
        it.title.contains(searchTerm, ignoreCase = true) ||
                it.body.contains(searchTerm, ignoreCase = true) ||
                it.description.contains(searchTerm, ignoreCase = true) ||
                it.published_date?.contains(searchTerm, ignoreCase = true) ?: false
    } as MutableList<Article>
}

fun Response.toJsonObject() : JSONObject? {
    try {
        val body = String(this.body().toByteArray(), Charsets.UTF_8)
        return JSONObject(body)
    } catch (e: java.lang.Exception) {
        println("Couldn't parse object.")
    }
    return null
}

fun JSONObject.toArticle() : Article {
    val article = Article()
    article.id = getSafeString("id")
    article.author = getSafeString("author")
    article.author = getSafeString("author")
    article.title = getSafeString("title")
    article.description = getSafeString("description")
    article.body = getSafeString("body").trim()
    article.published_date = getSafeString("published_date")
    article.url = getSafeString("url")
    article.imgUrl = getSafeString("img_url", default = getSafeString("imgUrl"))
    article.source = getSafeString("source")
    article.source_rank = getSafeString("source_rank")
    article.category = getSafeString("category")
    article.sentiment = getSafeString("sentiment")
    article.score = getSafeDouble("score")
    article.title_score = getSafeDouble("title_score")
    article.description_score = getSafeDouble("description_score")
    article.body_score = getSafeDouble("body_score")
    article.rank = getSafeDouble("rank")
    return article
}
