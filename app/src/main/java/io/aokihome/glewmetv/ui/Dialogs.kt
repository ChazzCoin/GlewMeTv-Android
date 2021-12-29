package io.aokihome.glewmetv.ui

import android.app.Activity
import android.app.Dialog
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Hookup
import kotlinx.android.synthetic.main.dialog_hookup_details.*

fun readHookupDialog(activity: Activity, hookup: Hookup?) : Dialog {
    val dialog = Dialog(activity)
    dialog.setContentView(R.layout.dialog_hookup_details)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    hookup?.let {
        dialog.txtHeader.text = it.title
        dialog.txtRank.text = "Rank: ${it.rank}"
        dialog.txtUrl.text = it.url
        dialog.txtBody.setText(it.body)
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
