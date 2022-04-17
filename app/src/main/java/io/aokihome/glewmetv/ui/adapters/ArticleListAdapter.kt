
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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Article
import io.aokihome.glewmetv.db.openArticle
import io.aokihome.glewmetv.db.saveToFavorites
import io.aokihome.glewmetv.ui.main.MainGlewMeTvActivity
import io.aokihome.glewmetv.utils.showSuccess


class ArticleListAdapter(var context: MainGlewMeTvActivity?,
                         var listOfArticles: MutableList<Article>? = null, var fragmentActivity: FragmentActivity):
        RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_articles, parent, false)
        return ArticleViewHolder(convertView = view, fragmentActivity)
    }

    override fun getItemCount(): Int {
        return listOfArticles?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        println("binding hookup")
        listOfArticles?.let { itArticles ->
            holder.bind(itArticles[position], context)

//            //onClickListener
//            holder.itemView.setOnClickListener {
//                context?.let {
//                    println("HOOKUP CLICKED: starting dialog now!")
//                    itArticles[position].openArticle()
////                    readArticleDialog(it, itArticles[position]).show()
//                }
//
//            }
        }
    }

    open class ArticleViewHolder(convertView: View, private val frag: FragmentActivity) : RecyclerView.ViewHolder(convertView) {
        //-> LEFT (TO)
        val textTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val textDate = itemView.findViewById<TextView>(R.id.txtPublishedDate)
        val textSource = itemView.findViewById<TextView>(R.id.txtSource)
        val imgUrl = itemView.findViewById<ImageView>(R.id.imgUrl)
        val btnSaveIcon = itemView.findViewById<ImageButton>(R.id.btnSaveIcon)

        fun bind(article: Article, context: Context?) {

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
                    Glide.with(frag).load(article.imgUrl).into(imgUrl)
                } catch (e: Exception) {
                    imgUrl.visibility = View.GONE
                    println("Failed to load image: $e")
                }
            } else {
                imgUrl.visibility = View.GONE
                println("Empty Img Url.")
            }

            textTitle.setOnClickListener {
                println("Article CLICKED: starting dialog now!")
                article.openArticle()
            }
            imgUrl.setOnClickListener {
                println("Article CLICKED: starting dialog now!")
                article.openArticle()
            }
            btnSaveIcon.setOnClickListener {
                article.saveToFavorites()
                showSuccess("Article Saved To Favorites!", context = context)
                println("Article Saved!")
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