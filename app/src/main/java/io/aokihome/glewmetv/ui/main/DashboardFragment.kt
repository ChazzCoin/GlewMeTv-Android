package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.ui.adapters.EventListAdapter
import io.aokihome.glewmetv.ui.adapters.TickerAdapter
import io.aokihome.glewmetv.utils.*
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_dashboard.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashboardFragment : Fragment() {

//    var hookupAdapter: HookupListAdapter? = null
    var listOfRealmArticles: RealmList<Article>? = null
    var listOfRealmTickers: RealmList<Ticker>? = null
    var listOfHookups = mutableListOf<Article>()
    var listOfEvents = mutableListOf<Event>()

    var listOfTickers = mutableListOf<Ticker>()
    var adapterTicker = TickerAdapter()
    var adapterEvents = EventListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session {
            listOfRealmTickers = it.tickers
            listOfRealmTickers?.let {
                listOfTickers = it.toMutableList()
            }

        }
        io {
            loadGlewMeTvData()
            println(listOfTickers)
            main {
                initTickers()
                initEvents()
            }
        }

    }

    private suspend fun loadGlewMeTvData() {
        val response = GmtHttpRequest().getGlewMeTvData().await()
        val parsedPackages = Parser.AllDataPackages(response)
        println(parsedPackages)
        listOfTickers = parsedPackages.PriceList
        listOfEvents = parsedPackages.EventList
    }

    fun initTickers() {
        adapterTicker = recyclerTickers.initTickers(listOfTickers)
    }

    fun initEvents() {
        adapterEvents = recyclerLiveEvents.initEvents(listOfEvents)
    }


}