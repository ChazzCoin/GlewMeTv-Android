
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
import io.aokihome.glewmetv.db.Article
import io.aokihome.glewmetv.ui.main.MainGlewMeTvActivity
import io.aokihome.glewmetv.ui.readArticleDialog
import io.realm.RealmList


class ArticleListAdapter(var context: MainGlewMeTvActivity?, var isTopTen: Boolean=false, var listOfArticles: MutableList<Article>? = null):
        RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_hookups2, parent, false)
        return ArticleViewHolder(convertView = view)
    }

    override fun getItemCount(): Int {
        return listOfArticles?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        println("binding hookup")
        listOfArticles?.let { itHookups ->
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

    class ArticleViewHolder(convertView: View, val isTopTen: Boolean=false) : RecyclerView.ViewHolder(convertView) {
        //-> LEFT (TO)
        val textTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val textDate = itemView.findViewById<TextView>(R.id.txtPublishedDate)
        val textSource = itemView.findViewById<TextView>(R.id.txtSource)
        val imgUrl = itemView.findViewById<ImageView>(R.id.imgUrl)

        fun bind(article: Article) {

            val sour = article.source.toString()
            if (sour.contains("twitter", ignoreCase = true)) {
                textTitle.text = article.body
            } else {
                textTitle.text = article.title
            }

            textSource.text = sour
            textDate.text = article.published_date
            if (article.imgUrl.isNotEmpty()) {
                try {
                    Picasso.get().load(article.imgUrl).fit().into(imgUrl)
                } catch (e: Exception) {
                    imgUrl.visibility = View.GONE
                    println("Failed to load image: $e")
                }
            } else {
                imgUrl.visibility = View.GONE
                println("Empty Img Url.")
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