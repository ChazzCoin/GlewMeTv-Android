package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.executeRealm
import io.aokihome.glewmetv.utils.executeRealmOnMain
import io.aokihome.glewmetv.utils.main
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONObject

/**
 * Created by ChazzCoin : December 2019.
 */
open class Session : RealmObject() {
    //DO NOT MAKE STATIC!
    @PrimaryKey
    var sessionId = 1

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

    /** -> EVERYTHING IS STATIC BELOW THIS POINT <- **/
    companion object {
        //Global App Setup
        private const val aisession = 1

        //Class Variables
        private var mRealm = Realm.getDefaultInstance()

        //GET CURRENT SESSION
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

fun getHookups() : RealmList<Article>? {
    return session()?.articles
}

fun getHookupsList() : MutableList<Article> {
    return getHookups().convertRealmListToList()
}

fun RealmList<Article>?.convertRealmListToList() : MutableList<Article> {
    val tempList = mutableListOf<Article>()
    this?.let {
        for (item in it) {
            tempList.add(item)
        }
    }
    return tempList
}

fun MutableList<Article>.prepHookupsForDisplay() {
    this.distinct().toList() // Remove any Duplicates
    this.sortBy { it.rank }  // Sort by Rank
    this.reversed()          // Reverse order, rank 1 will be at the bottom otherwise.
}

fun MutableList<Article>.topTen(): MutableList<Article> {
    val listCount = this.count()
    val highest = if (listCount < 10) listCount else 10
    return this.subList(0, highest)       // Reverse order, rank 1 will be at the bottom otherwise.
}

fun MutableList<Article>.removeTopTen(): MutableList<Article> {
    val listCount = this.count()
    if (listCount <= 10) return this
    val highest = if (listCount < 10) listCount else 10
    return this.subList(highest, this.indices.last)       // Reverse order, rank 1 will be at the bottom otherwise.
}

inline fun session(crossinline block: (Session) -> Unit): Unit? {
    return Session.session?.let {
        main {
            block(it)
        }
    }
}

fun session(): Session? {
    return Session.session
}

/** -> Hookups <- **/
fun addArticleToSessionOnMain(article: Article) {
    val session = Session.session
    executeRealmOnMain { itRealm ->
        if (session != null) {
            session.articles?.add(article)
            itRealm.copyToRealmOrUpdate(session) }
    }
}
/** -> Tickers <- **/
fun addTickerToSessionOnMain(ticker: Ticker) {
    val session = Session.session
    executeRealmOnMain { itRealm ->
        if (session != null) {
            session.tickers?.add(ticker)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

fun addTickerToSession(ticker: Ticker) {
    val session = Session.session
    executeRealm { itRealm ->
        if (session != null) {
            session.tickers?.add(ticker)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

/** -> Metaverse <- **/
fun addMetaverseToSessionOnMain(metaverse: Metaverse) {
    val session = Session.session
    executeRealmOnMain { itRealm ->
        if (session != null) {
            session.metaverses?.add(metaverse)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

fun addMetaverseToSession(metaverse: Metaverse) {
    val session = Session.session
    executeRealm { itRealm ->
        if (session != null) {
            session.metaverses?.add(metaverse)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

/** -> GlewMe <- **/
fun addGlewMeToSessionOnMain(glewme: GlewMe) {
    val session = Session.session
    executeRealmOnMain { itRealm ->
        if (session != null) {
            session.glewmes?.add(glewme)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

fun addGlewMeToSession(glewme: GlewMe) {
    val session = Session.session
    executeRealm { itRealm ->
        if (session != null) {
            session.glewmes?.add(glewme)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

fun saveSessionOnMain() {
    val session = Session.session
    executeRealmOnMain { itRealm ->
        if (session != null) {
            itRealm.copyToRealmOrUpdate(session)
        }
    }
}

fun removeAllHookupsOnMain() {
    val session = Session.session
    executeRealmOnMain {
        session?.articles?.clear()
    }
}

fun removeAllTickersOnMain() {
    val session = Session.session
    executeRealmOnMain {
        session?.tickers?.clear()
    }
}

//fun createHookupObjectOnMain(obj: JSONArray) {
//    executeRealmOnMain { itRealm ->
//        itRealm.createAllFromJson(Hookup::class.java, obj)
//
//    }
//}

fun createHookupObjectOnMain(obj: JSONObject) {
    executeRealmOnMain { itRealm ->
        itRealm.createObjectFromJson(Article::class.java, obj)
    }
}

//CREATE NEW SESSION
//fun createSession(): Session? {
//    if (Session.mRealm == null) { Session.mRealm = Realm.getDefaultInstance() }
//    val session = Session.session
//    session?.let { itSession ->
//        Session.mRealm.beginTransaction()
//        session.hookups = RealmList()
//        Session.mRealm.copyToRealmOrUpdate(session)
//        Session.mRealm.commitTransaction()
//    }
//    return session
//}

//fun createHookup() {
//    val realm = Realm.getDefaultInstance()
//    if (realm.where(Hookup::class.java) == null){
//        realm.executeTransaction { itRealm ->
//            itRealm.createObject(Hookup::class.java)
//        }
//    }
//}

//fun createSess() {
//    val realm = Realm.getDefaultInstance()
//    if (realm.where(Session::class.java) == null){
//        realm.executeTransaction { itRealm ->
//            itRealm.createObject(Session::class.java)
//        }
//    }
//}
