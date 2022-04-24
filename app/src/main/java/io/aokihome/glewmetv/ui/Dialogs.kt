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
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Article
import io.aokihome.glewmetv.utils.toJSON
import io.realm.RealmObject
import kotlinx.android.synthetic.main.dialog_hookup_details.*
import kotlinx.android.synthetic.main.read_article.*
import org.json.JSONObject


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

fun RealmObject.openJsonVersion(activity: Activity) : Dialog? {
    val jsonObj = this.toJSON() ?: return null
    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.dialog_hookup_details)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    dialog.txtJson.text = jsonObj.toString(4)
    return dialog
}

fun JSONObject.openJsonString(activity: Activity) : Dialog {
    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.dialog_hookup_details)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    dialog.txtJson.text = this.toString(4)
    return dialog
}
