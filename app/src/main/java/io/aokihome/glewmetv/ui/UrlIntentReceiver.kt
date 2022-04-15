package io.aokihome.glewmetv.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.utils.ArticleParser
import io.aokihome.glewmetv.utils.io
import kotlinx.android.synthetic.main.activity_url_intent_receiver.*

class UrlIntentReceiver : AppCompatActivity() {

    private var currentUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_intent_receiver)

        intent?.let {
            if (it.hasExtra(Intent.EXTRA_TEXT)) {
                currentUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                txtUrlToDownload.text = currentUrl
            }
        }


        btnCancel.setOnClickListener {
            txtUrlToDownload.text = "Not Url Set!"
            currentUrl = ""
        }
        btnDownloadUrl.setOnClickListener {
            if (currentUrl.isEmpty()) return@setOnClickListener
            downloadUrl(currentUrl)
        }
    }
    private fun downloadUrl(url: String) {
        io {
            val response = GmtHttpRequest().downloadUrlAsync(url).await()
            val arts = ArticleParser(response)
            println(arts)
        }
    }
}