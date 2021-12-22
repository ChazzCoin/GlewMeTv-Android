package io.aokihome.glewmetv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aokihome.glewmetv.models.Hookup
import kotlinx.android.synthetic.main.activity_hookup.*

class HookupActivity : AppCompatActivity() {

    companion object {
        var staticHookup: Hookup? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hookup)
        setSupportActionBar(findViewById(R.id.toolbar))

        staticHookup?.let {
            txtHeader.text = it.title
            txtRank.text = "Rank: ${it.rank}"
            txtUrl.text = it.url
            txtBody.setText(it.body)
            txtTopic.text = "Source: ${it.source}"
            txtAuthor.text = it.author
            txtCategory.text = it.category
            txtSentiment.text = it.sentiment
            txtDateTime.text = it.published_date
        } ?: run {
            onBackPressed()
        }

    }
}