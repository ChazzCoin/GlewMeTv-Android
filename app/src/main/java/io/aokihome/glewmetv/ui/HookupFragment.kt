package io.aokihome.glewmetv.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.Article

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HookupFragment : Fragment() {

    companion object {
        var staticArticle: Article? = null
    }
    var article: Article? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.dialog_hookup_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        staticHookup?.let {
//            txtHeader.text = it.title
//            txtRank.text = "Rank: ${it.rank}"
//            txtUrl.text = it.url
//            txtBody.setText(it.body)
//            txtTopic.text = "Source: ${it.source}"
//            txtAuthor.text = it.author
//            txtCategory.text = it.category
//            txtSentiment.text = it.sentiment
//            txtDateTime.text = it.published_date
//        } ?: run {
////            onBackPressed()
//        }
    }
}