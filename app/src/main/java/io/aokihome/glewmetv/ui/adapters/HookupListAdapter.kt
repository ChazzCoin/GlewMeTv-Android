
package io.aokihome.glewmetv.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Hookup
import io.aokihome.glewmetv.db.removeTopTen
import io.aokihome.glewmetv.ui.main.MainGlewMeTvActivity
import io.aokihome.glewmetv.ui.readArticleDialog
import io.realm.RealmList


class HookupListAdapter(var context: MainGlewMeTvActivity?, var isTopTen: Boolean=false, var listOfHookups: MutableList<Hookup>? = null):
        RecyclerView.Adapter<HookupListAdapter.HookupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HookupViewHolder {

        if (isTopTen) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_headline, parent, false)
            return HookupViewHolder(convertView = view)
        }
        listOfHookups = listOfHookups?.removeTopTen()
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_hookups2, parent, false)
        return HookupViewHolder(convertView = view)
    }

    override fun getItemCount(): Int {
        return listOfHookups?.size ?: 0
    }

    fun loadRealmHookups(newRealmHookups: RealmList<Hookup>) {
        listOfHookups?.clear()
        for (item in newRealmHookups) {
            listOfHookups?.add(item)
        }
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: HookupViewHolder, position: Int) {
        println("binding hookup")
        listOfHookups?.let { itHookups ->
            holder.bind(itHookups[position])
            //onClickListener
            holder.itemView.setOnClickListener {
                context?.let {
                    println("HOOKUP CLICKED: starting dialog now!")
                    readArticleDialog(it, itHookups[position]).show()
                }

            }
        }
    }

    class HookupViewHolder(convertView: View, val isTopTen: Boolean=false) : RecyclerView.ViewHolder(convertView) {
        //-> LEFT (TO)
        val textTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val textRank = itemView.findViewById<TextView>(R.id.txtAuthor)
        val textDate = itemView.findViewById<TextView>(R.id.txtPublishedDate)
        val textSource = itemView.findViewById<TextView>(R.id.txtSource)
        val imgUrl = itemView.findViewById<ImageView>(R.id.imgUrl)
        val itemLinearLayout = itemView.findViewById<LinearLayout>(R.id.itemLinearLayout)

        fun bind(hookup: Hookup) {
            textTitle.text = hookup.title
            textRank.text = "Rank: ${hookup.rank}"
            textSource.text = hookup.source.toString()
            textDate.text = hookup.published_date
            if (hookup.imgUrl.isNotEmpty()) {
                try {
                    Picasso.get().load(hookup.imgUrl).fit().into(imgUrl)
                } catch (e: Exception) {
                    println("Failed to load image: $e")
                }
            }

        }
    }
}


class CustomLayout : LinearLayout, Target {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
        background = BitmapDrawable(resources, bitmap)
    }

    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
        println("onBitmapFailed $e")
    }

    fun onBitmapFailed(errorDrawable: Drawable?) {
        //Set your error drawable
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        //Set your placeholder
    }
}