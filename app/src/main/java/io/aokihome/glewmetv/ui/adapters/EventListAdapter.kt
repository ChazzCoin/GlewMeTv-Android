
package io.aokihome.glewmetv.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Event
import io.aokihome.glewmetv.utils.onClickOpener


class EventListAdapter(var listOfEvents: MutableList<Event>? = null):
        RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_tv_banner, parent, false)
        return EventViewHolder(convertView = view)
    }

    override fun getItemCount(): Int {
        return listOfEvents?.size ?: 0
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        println("binding hookup")
        listOfEvents?.let { itEvents ->
            holder.bind(itEvents[position])
        }
    }

    class EventViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        //-> LEFT (TO)
//        val txtEventTitle = itemView.findViewById<TextView>(R.id.txtEventTitle)
//        val txtEventDate = itemView.findViewById<TextView>(R.id.txtEventDate)
        val txtEventLive = itemView.findViewById<TextView>(R.id.txtEventLive)
//        val txtEventPosition = itemView.findViewById<TextView>(R.id.txtEventPosition)
//        val txtEventMetaverseName = itemView.findViewById<TextView>(R.id.txtEventMetaverseName)
        val imgUrl = itemView.findViewById<ImageView>(R.id.imgEventImage)
//        val itemLinearLayout = itemView.findViewById<LinearLayout>(R.id.itemLinearLayout)

        fun bind(event: Event) {
//            txtEventTitle.text = event.name
//            txtEventDate.text = event.start_at
            if (event.live) txtEventLive.visibility = View.VISIBLE else txtEventLive.visibility = View.INVISIBLE
//            txtEventPosition.text = "${event.x}, ${event.y}"
//            txtEventMetaverseName.text = "Decentraland"
            if (event.image.isNotEmpty()) {
                try {
                    Glide.with(imgUrl.context).load(event.image).into(imgUrl)
                } catch (e: Exception) {
                    println("Failed to load image: $e")
                }
            }
            imgUrl.onClickOpener(event)

        }
    }
}
