package io.aokihome.glewmetv.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import io.aokihome.glewmetv.db.Hookup
import io.aokihome.glewmetv.db.Session
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

inline fun main(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
        block(this)
    }
}

inline fun ioLaunch(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
        block(this)
    }
}

fun Any?.isNullOrEmpty() : Boolean {
    if (this == null) return true
    when (this) {
        is String -> { if (this.isEmpty() || this.isBlank()) return true }
        is Collection<*> -> { if (this.isEmpty()) return true }
        is RealmList<*> -> { if (this.isEmpty()) return true }
    }
    return false
}


fun realm() : Realm {
    return Realm.getDefaultInstance()
}

inline fun executeRealm(crossinline block: (Realm) -> Unit) {
    main {
        realm().executeTransaction {
            block(it)
        }
    }
}

fun showFailedToast(context: Context, mess: String = "There was an Error.") {
    Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
}

fun showSuccess(context: Context, mess: String = "Success!") {
    Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
}

// -> JSON
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

fun JSONObject.getSafeBoolean(obj: String) : Boolean {
    if (this.has(obj)) {
        return this.getBoolean(obj)
    }
    return false
}

fun JSONObject.getSafeJsonObj(obj: String) : JSONObject? {
    if (this.has(obj)) {
        return this.getJSONObject(obj)
    }
    return null
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
