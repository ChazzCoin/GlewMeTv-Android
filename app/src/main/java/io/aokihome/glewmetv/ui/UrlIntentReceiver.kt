package io.aokihome.glewmetv.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aokihome.glewmetv.MainActivity
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.openArticle
import io.aokihome.glewmetv.db.openArticleContext
import io.aokihome.glewmetv.db.saveToFavorites
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.utils.ArticleParser
import io.aokihome.glewmetv.utils.io
import io.aokihome.glewmetv.utils.main
import kotlinx.android.synthetic.main.activity_url_intent_receiver.*

class UrlIntentReceiver : AppCompatActivity() {

    private var currentUrl: String = ""
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_intent_receiver)

        context = this

        intent?.let {
            if (it.hasExtra(Intent.EXTRA_TEXT)) {
                currentUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                txtUrlToDownload.text = currentUrl
            }
        }
        btnCancel.setOnClickListener {
            txtUrlToDownload.text = "Not Url Set!"
            currentUrl = ""
            startActivity(Intent(this, MainActivity::class.java))
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
            if (!arts.isNullOrEmpty()){
                context?.let { itContext ->

                    main {
                        val downloadedArt = arts.first()
                        downloadedArt.saveToFavorites()
                        downloadedArt.openArticleContext(itContext)
                    }

                }
            }
            println(arts)
        }
    }
}