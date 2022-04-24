package io.aokihome.glewmetv.db

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataProvider: RealmObject() {

    //DO NOT MAKE STATIC!
    @PrimaryKey
    var dataID = 1

    var GLEWMECITY : GlewMe? = GlewMe()
    var GLEWMTV : RealmList<Metaverse>? = RealmList()
    var EVENTS : RealmList<Event>? = RealmList()
    var METAVERSES : RealmList<Metaverse>? = RealmList()

}