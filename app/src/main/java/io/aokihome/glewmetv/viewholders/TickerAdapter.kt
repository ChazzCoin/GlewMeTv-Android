
package io.aokihome.glewmetv.viewholders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.models.Ticker
import org.jetbrains.anko.textColor


class TickerAdapter(): RecyclerView.Adapter<TickerAdapter.TickerViewHolder>() {

    var listOfTickers: List<Ticker>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_ticker_scroll, parent, false)
        return TickerViewHolder(convertView = view)
    }

    fun addListOfTickers(tickers: List<Ticker>) {
        listOfTickers = tickers
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listOfTickers?.size ?: 0
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        println("binding hookup")
        listOfTickers?.let { itTickers ->
            holder.bind(itTickers[position])
            //onClickListener
            holder.itemView.setOnClickListener {
                //go to ticker activity
            }
        }
    }

    class TickerViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        //-> LEFT (TO)
        val textTicker = itemView.findViewById<TextView>(R.id.txtTickerName)
        val textPrice = itemView.findViewById<TextView>(R.id.txtTickerPrice)

        fun bind(ticker: Ticker) {
            textTicker.text = ticker.name
            if (ticker.isPos) {
                textPrice.textColor = Color.GREEN
            } else {
                textPrice.textColor = Color.RED
            }
            textPrice.text = ticker.price
        }
    }
}