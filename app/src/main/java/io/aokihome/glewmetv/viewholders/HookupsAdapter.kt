
package io.aokihome.glewmetv.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.aokihome.glewmetv.MainActivity
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.models.Hookup
import io.aokihome.glewmetv.ui.HookupFragment
import io.aokihome.glewmetv.ui.MetaReportFragment


class HookupAdapter(var context: MetaReportFragment?, var listOfHookups: List<Hookup>? = null):
        RecyclerView.Adapter<HookupAdapter.HookupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HookupViewHolder {
        val chatView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_list_hookups,
            parent,
            false
        )
        return HookupViewHolder(convertView = chatView)
    }

    override fun getItemCount(): Int {
        return listOfHookups?.size ?: 0
    }

    override fun onBindViewHolder(holder: HookupViewHolder, position: Int) {
        println("binding hookup")
        listOfHookups?.let { itHookups ->
            holder.bind(itHookups[position])
            //onClickListener
            holder.itemView.setOnClickListener {
//                HookupActivity.staticHookup = itHookups[position]
                val frag = HookupFragment()
                frag.hookup = itHookups[position]
                context?.childFragmentManager
                    ?.beginTransaction()
                    ?.add(R.id.hookupFragment, frag)
                    ?.addToBackStack(null)
                    ?.commit()

            }
        }
    }

    class HookupViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        //-> LEFT (TO)
        val textTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val textRank = itemView.findViewById<TextView>(R.id.txtRank)
        val textDate = itemView.findViewById<TextView>(R.id.txtDate)
        val textSource = itemView.findViewById<TextView>(R.id.txtSource)
        val imgUrl = itemView.findViewById<ImageView>(R.id.imgUrl)
        val itemLinearLayout = itemView.findViewById<LinearLayout>(R.id.itemLinearLayout)

        fun bind(hookup: Hookup) {
            textTitle.text = hookup.title
            textRank.text = "Rank: ${hookup.rank}"
            textSource.text = hookup.source.toString()
            textDate.text = hookup.published_date
            if (!hookup.imgUrl.isNullOrEmpty()) {
                Picasso.get().load(hookup.imgUrl).fit().into(imgUrl)
            }

        }
    }
}