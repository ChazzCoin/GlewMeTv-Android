package io.aokihome.glewmetv.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.models.Ticker
import io.aokihome.glewmetv.viewholders.HookupAdapter
import io.aokihome.glewmetv.viewholders.TickerAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashboardFragment : Fragment() {

    val testList = mutableListOf<Ticker>()

    val listOfTickers: List<Ticker>? = null
//    val layout = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val adapter = TickerAdapter()

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
        initTickers()
    }

    fun getTestTicker(name:String, price:String, isPos:Boolean) : Ticker {
        val ticker = Ticker()
        ticker.apply {
            this.name = name
            this.price = price
            this.isPos = isPos
        }
        return ticker
    }

    fun initTickers() {
        testList?.let { itTickers ->
            recyclerTickers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerTickers.adapter = adapter
            adapter.addListOfTickers(itTickers)
        }

    }

}