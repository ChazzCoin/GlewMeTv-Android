package io.aokihome.glewmetv.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.RiptHttpRequest
import io.aokihome.glewmetv.utils.*
import io.aokihome.glewmetv.ui.adapters.HookupListAdapter
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_metareport.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.json.JSONObject
import kotlin.collections.isNullOrEmpty

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MetaReportFragment() : Fragment() {
    var session: Session? = null

    var hookupAdapter: HookupListAdapter? = null
    var listOfRealmHookups: RealmList<Hookup>? = null
    var listOfHookups = mutableListOf<Hookup>()
    val io = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metareport, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtLoadOnMain("Initiating Session..." )
        session = Session.session
//        runLoadHookupsAsync()
        session {
            if (!it.hookups.isNullOrEmpty()) {
                listOfRealmHookups = it.hookups
                txtLoadOnMain("Setting Up Saved Hookups from Realm...")
                setupRealmHookupAdapter()
                return
            } else {
                txtLoadOnMain("Session found but no hookups, loading new hookups...")
                runLoadHookupsAsync()
                return
            }
        }

        txtLoadOnMain("No session found, loading new hookups...")
        runLoadHookupsAsync()

    }

    fun runLoadHookupsAsync() {
        ioLaunch { loadHookups() }
    }

    private fun txtLoadOnMain(text:String) {
        main { txtLoad.text = text}
    }

    private suspend fun loadHookups() {
        txtLoadOnMain("Loading Hookups...")
        listOfHookups.clear()
        removeAllHookups()
        val response = RiptHttpRequest().getAsync(RiptHttpRequest.VAATU_BASE_URL).await()
        val listOfJsonObjs = parseToJSON(response)
        txtLoadOnMain("Parsing Hookups...")
        for (hookupObj in listOfJsonObjs) {
            val item = hookupObj
            try {
                val jsonObject = JSONObject(item.toString())
                val tempHookup = jsonObject.parseToHookup()
                main{
                    listOfHookups.add(tempHookup)
                    addHookupToSessionOnMain(tempHookup)
                }
            } catch (e: Exception) {
                println("failed: $e")
            }
        }
        txtLoadOnMain("Setting Up RecyclerView...")
        main { setupHookupAdapter() }
    }

    private fun setupHookupAdapter() {
        listOfHookups.filter { it.source.toString() != "Twitter" }
        hookupAdapter = HookupListAdapter(context=MainGlewMeTvActivity.context, listOfHookups=listOfHookups)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = hookupAdapter
        hookupAdapter?.notifyDataSetChanged()
        txtLoad.text = "DONE!"
    }

    private fun setupRealmHookupAdapter() {
//        listOfHookups.filter { it.source.toString() != "Twitter" }
        listOfHookups = listOfRealmHookups.convertRealmListToList()
        listOfHookups.toSet().toList()
        listOfHookups.sortBy { it.rank }
        listOfHookups.reversed()
        hookupAdapter = HookupListAdapter(context=MainGlewMeTvActivity.context, listOfHookups=listOfHookups)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = hookupAdapter
        hookupAdapter?.notifyDataSetChanged()
        txtLoad.text = "DONE!"

    }

}

fun RealmList<Hookup>?.convertRealmListToList() : MutableList<Hookup> {
    val tempList = mutableListOf<Hookup>()
    this?.let {
        for (item in it) {
            tempList.add(item)
        }
    }
    return tempList
}