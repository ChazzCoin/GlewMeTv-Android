package io.aokihome.glewmetv.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.aokihome.glewmetv.MainActivity
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.http.RiptHttpRequest
import io.aokihome.glewmetv.models.Hookup
import io.aokihome.glewmetv.models.parseToHookup
import io.aokihome.glewmetv.viewholders.HookupAdapter
import kotlinx.android.synthetic.main.fragment_metareport.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MetaReportFragment(val mainActivity: MainActivity?=null) : Fragment() {
    private val job = SupervisorJob()
    val riptDispatcher = CoroutineScope(Dispatchers.IO + job)
    val mainDispatcher = CoroutineScope(Dispatchers.Main + job)
    var hookupAdapter: HookupAdapter? = null
    val listOfHookups = mutableListOf<Hookup>()
//    val mainActivity: MainActivity? = null

    //recyclerView
//    val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//    val adapter = HookupAdapter(this, listOfHookups)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metareport, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        riptDispatcher.launch {
            loadHookups()
        }
    }


    private suspend fun loadHookups() {
        listOfHookups.clear()
        val response = RiptHttpRequest().getAsync(RiptHttpRequest.VAATU_BASE_URL).await()
        val listOfHookupsObjs = RiptHttpRequest.parseHookups(response)

        for (hookup in listOfHookupsObjs) {
            val item = hookup
            try {
                val jsonObject = JSONObject(item.toString())
                val tempHookup = jsonObject.parseToHookup()
                listOfHookups.add(tempHookup)
                val test = jsonObject.get("title")
                println(test)
            } catch (e: Exception) {
                println("failed")
            }
            println(item)
        }
        println(listOfHookups)
        mainDispatcher.launch {
            setupHookupAdapter()
        }

    }
    private fun setupHookupAdapter() {
        listOfHookups.filter { it.source.toString() != "Twitter" }
//        listOfHookups.sortBy { it.rank }
//        listOfHookups.reverse()
        hookupAdapter = HookupAdapter(this, listOfHookups)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = hookupAdapter
        hookupAdapter?.notifyDataSetChanged()

    }
}