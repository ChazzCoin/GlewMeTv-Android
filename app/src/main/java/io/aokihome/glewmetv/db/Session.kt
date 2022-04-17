package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.executeRealmOnMain
import io.aokihome.glewmetv.utils.main
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by ChazzCoin : December 2019.
 */
open class Session : RealmObject() {
    //DO NOT MAKE STATIC!
    @PrimaryKey
    var sessionId = 1

    var cacheArticles : RealmList<Article>? = RealmList()
    var savedArticles : RealmList<Article>? = RealmList()
    var articles : RealmList<Article>? = RealmList()
    var tickers : RealmList<Ticker>? = RealmList()
    var metaverses : RealmList<Metaverse>? = RealmList()
    var glewmes : RealmList<GlewMe>? = RealmList()

    init {
        articles = RealmList<Article>()
        tickers = RealmList<Ticker>()
        metaverses = RealmList()
        glewmes = RealmList()

    }

    /** -> STATIC <- **/
    companion object {
        //Global App Setup
        private const val aisession = 1

        //Class Variables
        private var mRealm = Realm.getDefaultInstance()

        /** -> TRIED AND TRUE! <- */
        var session: Session? = null
            get() {
                try {
                    if (mRealm == null) { mRealm = Realm.getDefaultInstance() }
                    field = mRealm.where(Session::class.java).equalTo("sessionId", aisession).findFirst()
                    if (field == null) {
                        field = Session()
                        field?.sessionId = aisession
                    }
                } catch (e: Exception) { e.printStackTrace() }
                return field
            }
            private set

    //END OF STATIC
    }
}
/**
 * SESSION EXTENSIONS
 * */


fun RealmList<Article>?.toList() : MutableList<Article> {
    val tempList = mutableListOf<Article>()
    this?.let {
        for (item in it) {
            tempList.add(item)
        }
    }
    return tempList
}

/** -> TRIED AND TRUE! <- */
fun List<Article>?.toRealmList() : RealmList<Article> {
    val realmList = RealmList<Article>()
    this?.let {
        for (item in it) {
            realmList.add(item)
        }
    }
    return realmList
}

/**
 * MASTER SAVING METHOD
 * -> TRIED AND TRUE!
 */
fun List<Article>.saveCached() {
    val sess = Session.session
    executeRealmOnMain {
        sess?.let { itSess ->
            itSess.cacheArticles?.addAll(this)
            val newList = itSess.cacheArticles?.distinct()
            itSess.cacheArticles = newList.toRealmList()
            it.copyToRealmOrUpdate(itSess)
        }
    }
}


/** -> TRIED AND TRUE! <- */
fun Article.saveToFavorites() {
    val sess = Session.session
    executeRealmOnMain {
        sess?.let { itSess ->
            itSess.savedArticles?.let { itSavedArticles ->
                if (!itSavedArticles.contains(this)) {
                    itSess.savedArticles?.add(this)
                    it.copyToRealmOrUpdate(itSess)
                }
            }
        }
    }
}

fun clearArticleCache() {
    val sess = Session.session
    executeRealmOnMain {
        sess?.let { itSess ->
            itSess.cacheArticles = RealmList()
            it.copyToRealmOrUpdate(itSess)
        }
    }
}

//
fun clearArticleFavorites() {
    val sess = Session.session
    executeRealmOnMain {
        sess?.let { itSess ->
            itSess.savedArticles = RealmList()
            it.copyToRealmOrUpdate(itSess)
        }
    }
}

/**
 * Main Safe Session Block
 */
inline fun session(crossinline block: (Session) -> Unit): Unit? {
    return Session.session?.let {
        main {
            block(it)
        }
    }
}

fun addArticleToSessionOnMain(article: Article) {
    val session = Session.session
    executeRealmOnMain { itRealm ->
        if (session != null) {
            session.articles?.add(article)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

fun removeAllTickersOnMain() {
    val session = Session.session
    executeRealmOnMain {
        session?.tickers?.clear()
    }
}




