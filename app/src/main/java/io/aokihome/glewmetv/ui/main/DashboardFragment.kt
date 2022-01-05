package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.ui.adapters.HookupListAdapter
import io.aokihome.glewmetv.ui.adapters.TickerAdapter
import io.aokihome.glewmetv.utils.*
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashboardFragment : Fragment() {

    var hookupAdapter: HookupListAdapter? = null
    var listOfRealmHookups: RealmList<Hookup>? = null
    var listOfRealmTickers: RealmList<Ticker>? = null
    var listOfHookups = mutableListOf<Hookup>()
    var listOfEvents = mutableListOf<Event>()

    var listOfTickers = mutableListOf<Ticker>()
    var adapterTicker = TickerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sess = Session.session
        session {
            listOfRealmTickers = it.tickers
            listOfRealmTickers?.let {
                listOfTickers = it.toMutableList()
//                for (item in it) {
//                    listOfTickers.add(item)
//                }
                initTickers()
                initEvents()
                initTopTen()
            }

        }
        io {
            loadGlewMeTvData()
            println(listOfTickers)
            main {
                initTickers()
                initEvents()
                initTopTen()
            }
        }

    }

    private suspend fun loadGlewMeTvData() {
        val response = GmtHttpRequest().getAsync(GmtHttpRequest.URL_GLEWMETV_DATA).await()
        val responseTwo = GmtHttpRequest().getAsync(GmtHttpRequest.URL_HOOKUPS_DATA).await()
        val parsedPackages = Parser.AllDataPackages(response)
        val parsedHookups = Parser.Hookups(responseTwo)
        println(parsedPackages)
        println(parsedHookups)
        listOfTickers = parsedPackages.PriceList
        listOfEvents = parsedPackages.EventList
        listOfHookups = parsedHookups.HookupList
    }

    fun initTickers() {
        adapterTicker = recyclerTickers.initTickers(listOfTickers)
    }

    fun initEvents() {
        var adapter = recyclerLiveEvents.initEvents(listOfEvents)
    }

    private fun initTopTen() {
        listOfHookups = getHookupsList()
        listOfHookups.prepHookupsForDisplay()
        listOfHookups = listOfHookups.topTen()
        hookupAdapter = recyclerHeadlines.initHookups(listOfHookups, true)
    }

}