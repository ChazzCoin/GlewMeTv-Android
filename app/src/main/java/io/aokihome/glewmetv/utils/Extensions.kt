package io.aokihome.glewmetv.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.aokihome.glewmetv.db.Event
import io.aokihome.glewmetv.db.Article
import io.aokihome.glewmetv.db.Ticker
import io.aokihome.glewmetv.ui.adapters.EventListAdapter
import io.aokihome.glewmetv.ui.main.MainGlewMeTvActivity
import io.aokihome.glewmetv.ui.adapters.ArticleListAdapter
import io.aokihome.glewmetv.ui.adapters.TickerAdapter
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_jarticle.*
import kotlinx.coroutines.*
import java.lang.Exception

inline fun tryCatch(block:() -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        println("Safe Extension Caught Exception: $e")
    }
}

/** -> TRIED AND TRUE! <- */
inline fun main(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
        block(this)
    }
}

/** -> TRIED AND TRUE! <- */
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

fun RecyclerView.setMargins(left:Int, top:Int, right:Int, bottom:Int) {
    val marginLayoutParams = ViewGroup.MarginLayoutParams(this.layoutParams)
    marginLayoutParams.setMargins(left, top, right, bottom)
    this.layoutParams = marginLayoutParams
}

/** -> TRIED AND TRUE! <- */
fun RecyclerView.initArticles(listOfArticles: MutableList<Article>, fragmentActivity: FragmentActivity) : ArticleListAdapter {
    val hookupAdapter = ArticleListAdapter(context = MainGlewMeTvActivity.context,
                                            listOfArticles = listOfArticles, fragmentActivity = fragmentActivity)
    this.layoutManager = LinearLayoutManager(MainGlewMeTvActivity.context, LinearLayoutManager.VERTICAL, false)
    this.adapter = hookupAdapter
    return hookupAdapter
}

fun RecyclerView.initEvents(listOfEvents: MutableList<Event>) : EventListAdapter {
    val eventAdapter = EventListAdapter(context = MainGlewMeTvActivity.context, listOfEvents = listOfEvents)
    this.layoutManager = LinearLayoutManager(MainGlewMeTvActivity.context, LinearLayoutManager.HORIZONTAL, false)
    this.adapter = eventAdapter
    return eventAdapter
}

fun RecyclerView.initTickers(listOfTickers: MutableList<Ticker>) : TickerAdapter {
    val adapter = TickerAdapter()
    this.layoutManager = LinearLayoutManager(MainGlewMeTvActivity.context, LinearLayoutManager.HORIZONTAL, false)
    this.adapter = adapter
    adapter.addListOfTickers(listOfTickers)
    return adapter
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/** -> TRIED AND TRUE! <- */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/** -> TRIED AND TRUE! <- */
fun <T> RealmList<T>?.toMutableList() : MutableList<T> {
    val listOfT = mutableListOf<T>()
    this?.let {
        for (item in it) {
            listOfT.add(item)
        }
    }
    return listOfT
}

/** -> TRIED AND TRUE! <- */
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

/** -> TRIED AND TRUE! <- */
fun realm() : Realm {
    return Realm.getDefaultInstance()
}

/** -> TRIED AND TRUE! <- */
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

/** -> TRIED AND TRUE! <- */
fun showSuccess(mess: String = "Success!", context: Context? = null) {
    context?.let {
        MainGlewMeTvActivity.context?.let {
            Toast.makeText(it, mess, Toast.LENGTH_SHORT).show()
        }
    } ?: run { Toast.makeText(context, mess, Toast.LENGTH_SHORT).show() }
}
fun toast(mess: String = "Uh OH!", context: Context? = null) {
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
