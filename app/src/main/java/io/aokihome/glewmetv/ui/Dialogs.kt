package io.aokihome.glewmetv.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Article
import kotlinx.android.synthetic.main.dialog_hookup_details.*
import kotlinx.android.synthetic.main.dialog_hookup_details.txtAuthor
import kotlinx.android.synthetic.main.read_article.*


@RequiresApi(Build.VERSION_CODES.N)
fun readArticleDialog(activity: Activity, article: Article?) : Dialog {
    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.read_article)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    article?.let {
        dialog.txtTitle.isClickable = true
        dialog.txtTitle.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a href='${it.url}'> ${it.title} </a>"
        dialog.txtTitle.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)

        dialog.txtArticleBody.text = it.body.trim()
        dialog.txtAuthor.text = it.author
        dialog.txtSource.text = it.source
        dialog.txtPublishedDate.text = it.published_date

        if (it.imgUrl.isNotEmpty()) {
            try {
                Glide.with(activity).load(it.imgUrl).into(dialog.imgUrl)
            } catch (e: Exception) {
                dialog.imgUrl.visibility = View.GONE
                println("Failed to load image: $e")
            }
        } else {
            dialog.imgUrl.visibility = View.GONE
            println("Empty Img Url.")
        }

    }

    return dialog
}

@RequiresApi(Build.VERSION_CODES.N)
fun readArticleDialog(activity: Context, article: Article?) : Dialog {
    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.read_article)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    article?.let {
        dialog.txtTitle.isClickable = true
        dialog.txtTitle.movementMethod = LinkMovementMethod.getInstance()
        val text = "<a href='${it.url}'> ${it.title} </a>"
        dialog.txtTitle.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)

        dialog.txtArticleBody.text = it.body.trim()
        dialog.txtAuthor.text = it.author
        dialog.txtSource.text = it.source
        dialog.txtPublishedDate.text = it.published_date

        if (it.imgUrl.isNotEmpty()) {
            try {
                Glide.with(activity).load(it.imgUrl).into(dialog.imgUrl)
            } catch (e: Exception) {
                dialog.imgUrl.visibility = View.GONE
                println("Failed to load image: $e")
            }
        } else {
            dialog.imgUrl.visibility = View.GONE
            println("Empty Img Url.")
        }

    }

    return dialog
}

fun readHookupDialog(activity: Activity, article: Article?) : Dialog {
    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.dialog_hookup_details)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    article?.let {
        dialog.txtHeader.text = it.title
        dialog.txtAuthor.text = "Rank: ${it.rank}"
        dialog.txtUrl.text = it.url
        dialog.txtBody.setText(it.body.trim())
        dialog.txtTopic.text = "Source: ${it.source}"
        dialog.txtAuthor.text = it.author
        dialog.txtCategory.text = it.category
        dialog.txtSentiment.text = it.sentiment
        dialog.txtDateTime.text = it.published_date
    }

    // On Clicks
//    val cancel = dialog.findViewById(R.id.btnCancelAskUser) as Button
//    yes.setOnClickListener {
//        Session.restartApplication(activity)
//    }
//    cancel.setOnClickListener {
//        dialog.dismiss()
//    }
    return dialog
}
