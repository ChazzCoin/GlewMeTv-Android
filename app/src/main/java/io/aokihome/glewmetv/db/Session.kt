package io.aokihome.glewmetv.db

import io.aokihome.glewmetv.utils.executeRealm
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by ChazzCoin : December 2019.
 */
open class Session : RealmObject() {
    //DO NOT MAKE STATIC!
    @PrimaryKey
    var sessionId = 1

    var hookups : RealmList<Hookup>? = RealmList()
    var tickers : RealmList<Ticker>? = RealmList()

    init {
        hookups = RealmList<Hookup>()
        tickers = RealmList<Ticker>()
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

inline fun session(block: (Session) -> Unit): Unit? {
    return Session.session?.let { block(it) }
}

fun session(): Session? {
    return Session.session
}

/** -> Hookups <- **/
fun addHookupToSessionOnMain(hookup: Hookup) {
    val session = Session.session
    executeRealm { itRealm ->
        if (session != null) {
            session.hookups?.add(hookup)
            itRealm.copyToRealmOrUpdate(session) }
    }
}

fun saveSession() {
    val session = Session.session
    executeRealm { itRealm ->
        if (session != null) {
            itRealm.copyToRealmOrUpdate(session)
        }
    }
}

fun removeAllHookups() {
    val session = Session.session
    executeRealm {
        session?.hookups?.clear()
    }
}

fun createHookupObject(obj: JSONArray) {
    executeRealm { itRealm ->
        itRealm.createAllFromJson(Hookup::class.java, obj)
    }
}

fun createHookupObject(obj: JSONObject) {
    executeRealm { itRealm ->
        itRealm.createObjectFromJson(Hookup::class.java, obj)
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
