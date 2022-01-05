package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.utils.*
import io.aokihome.glewmetv.ui.adapters.HookupListAdapter
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_metareport.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

//        txtLoadOnMain("Initiating Session..." )
        session = Session.session
        session {
            if (!it.hookups.isNullOrEmpty()) {
                listOfRealmHookups = it.hookups
//                txtLoadOnMain("Setting Up Saved Hookups from Realm...")
                setupRealmHookupAdapter()
            } else {
//                txtLoadOnMain("Session found but no hookups, loading new hookups...")
                runLoadHookupsAsync()
            }
        } ?: run {
//            txtLoadOnMain("No session found, loading new hookups...")
            runLoadHookupsAsync()
        }
    }

    fun runLoadHookupsAsync() {
        io { loadHookups() }
    }

//    private fun txtLoadOnMain(text:String) {
//        main { txtLoad.text = text}
//    }

    private suspend fun loadHookups() {
//        txtLoadOnMain("Loading Hookups...")
        listOfHookups.clear()
        removeAllHookupsOnMain()
        val response = GmtHttpRequest().getAsync(GmtHttpRequest.URL_HOOKUPS_DATA).await()
//        txtLoadOnMain("Parsing Hookups...")
        Parser.Hookups(response)
//        txtLoadOnMain("Setting Up RecyclerView...")
        main { setupHookupAdapter() }
    }

    private fun setupHookupAdapter() {
        listOfHookups.filter { it.source.toString() != "Twitter" }
        hookupAdapter = HookupListAdapter(context= MainGlewMeTvActivity.context, listOfHookups=listOfHookups)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = hookupAdapter
        hookupAdapter?.notifyDataSetChanged()
        showSuccess("New Hookups Loaded from Server!", MainGlewMeTvActivity.context)
    }

    private fun setupRealmHookupAdapter() {
        listOfHookups = getHookupsList()
        listOfHookups.prepHookupsForDisplay()
        listOfHookups.filterOutSource("Twitter")
        hookupAdapter = HookupListAdapter(context= MainGlewMeTvActivity.context, listOfHookups=listOfHookups)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = hookupAdapter
        hookupAdapter?.notifyDataSetChanged()
        showSuccess("Realm Hookups Loaded!", MainGlewMeTvActivity.context)
    }

}

