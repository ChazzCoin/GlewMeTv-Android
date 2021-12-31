package io.aokihome.glewmetv.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.aokihome.glewmetv.db.Hookup
import io.aokihome.glewmetv.db.Ticker
import io.aokihome.glewmetv.ui.MainGlewMeTvActivity
import io.aokihome.glewmetv.ui.adapters.HookupListAdapter
import io.aokihome.glewmetv.ui.adapters.TickerAdapter
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.*


inline fun main(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
        block(this)
    }
}

inline fun io(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
        block(this)
    }
}

suspend inline fun await(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO + SupervisorJob()).async {
        block(this)
    }.await()
}

fun RecyclerView.initHookups(listOfHookups: MutableList<Hookup>, isTopTen: Boolean = false) : HookupListAdapter {
    val hookupAdapter = HookupListAdapter(context = MainGlewMeTvActivity.context, isTopTen = isTopTen, listOfHookups = listOfHookups)
    this.layoutManager = LinearLayoutManager(
            MainGlewMeTvActivity.context,
            if (isTopTen) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL,
            false)
    this.adapter = hookupAdapter
    hookupAdapter.notifyDataSetChanged()
    return hookupAdapter
}

fun RecyclerView.initTickers(listOfTickers: MutableList<Ticker>) : TickerAdapter {
    val adapter = TickerAdapter()
    this.layoutManager = LinearLayoutManager(MainGlewMeTvActivity.context, LinearLayoutManager.HORIZONTAL, false)
    this.adapter = adapter
    adapter.addListOfTickers(listOfTickers)
    return adapter
}

fun Any?.isNullOrEmpty() : Boolean {
    if (this == null) return true
    when (this) {
        is String -> {
            if (this.isEmpty() || this.isBlank()) return true
        }
        is Collection<*> -> {
            if (this.isEmpty()) return true
        }
        is RealmList<*> -> {
            if (this.isEmpty()) return true
        }
    }
    return false
}

fun realm() : Realm {
    return Realm.getDefaultInstance()
}

inline fun executeRealmOnMain(crossinline block: (Realm) -> Unit) {
    main {
        realm().executeTransaction {
            block(it)
        }
    }
}

inline fun executeRealm(crossinline block: (Realm) -> Unit) {
    Realm.getDefaultInstance().executeTransaction {
        block(it)
    }
}

fun showFailedToast(context: Context, mess: String = "There was an Error.") {
    Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
}

fun showSuccess(mess: String = "Success!", context: Context? = null) {
    context?.let {
        MainGlewMeTvActivity.context?.let {
            Toast.makeText(it, mess, Toast.LENGTH_SHORT).show()
        }
    } ?: run { Toast.makeText(context, mess, Toast.LENGTH_SHORT).show() }
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
