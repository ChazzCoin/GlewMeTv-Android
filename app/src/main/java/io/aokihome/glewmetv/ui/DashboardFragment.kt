package io.aokihome.glewmetv.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.ui.adapters.HookupListAdapter
import io.aokihome.glewmetv.ui.adapters.TickerAdapter
import io.aokihome.glewmetv.utils.initHookups
import io.aokihome.glewmetv.utils.initTickers
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashboardFragment : Fragment() {

    val testList = mutableListOf<Ticker>()

    var hookupAdapter: HookupListAdapter? = null
    var listOfRealmHookups: RealmList<Hookup>? = null
    var listOfRealmTickers: RealmList<Ticker>? = null
    var listOfHookups = mutableListOf<Hookup>()

    val listOfTickers = mutableListOf<Ticker>()
    var adapter = TickerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testList.add(getTestTicker("MANA", "$2.24", false))

        testList.add(getTestTicker("XRP", "$0.32", true))
        testList.add(getTestTicker("SAND", "$25.24", true))
        testList.add(getTestTicker("GME", "$5.32", false))
        testList.add(getTestTicker("ETH", "$2.24", false))
        testList.add(getTestTicker("ROBO", "$22.32", false))
        testList.add(getTestTicker("BTC", "$100,000.24", false))
        testList.add(getTestTicker("LTC", "$189.32", true))
//        val sess = Session.session
        session {
            listOfRealmTickers = it.tickers
            listOfRealmTickers?.let {
                for (item in it) {
                    listOfTickers.add(item)
                }
            }

        }
        initTickers()
        setupRealmHookupAdapter()

    }

    fun getTestTicker(name:String, price:String, isPos:Boolean) : Ticker {
        val ticker = Ticker()
        ticker.apply {
            this.name = name
            this.price = price
            this.isPos = isPos
        }
        addTickerToSessionOnMain(ticker)
        return ticker
    }

    fun initTickers() {
        adapter = recyclerTickers.initTickers(testList)
    }

    private fun setupRealmHookupAdapter() {
        listOfHookups = getHookupsList()
        listOfHookups.prepHookupsForDisplay()
        listOfHookups.filterOutSource("Twitter")
        listOfHookups = listOfHookups.topTen()
        hookupAdapter = recyclerHeadlines.initHookups(listOfHookups, true)
        recyclerEvents.initHookups(listOfHookups, false)
    }

}