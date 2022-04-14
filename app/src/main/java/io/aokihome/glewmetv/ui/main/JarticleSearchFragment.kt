package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.utils.*
import io.aokihome.glewmetv.ui.adapters.ArticleListAdapter
import kotlinx.android.synthetic.main.fragment_metareport.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class JarticleSearchFragment() : Fragment() {
    var session: Session? = null

    var articleAdapter: ArticleListAdapter? = null
    private var lockedListOfArticles: List<Article>? = null
    var filteredList = mutableListOf<Article>()
    val io = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metareport, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // WORKING! SEARCH!
        btnSearchIcon.setOnClickListener {
            hideKeyboard()
            this.externalSearchArticlesAsync()
        }
        btnImgDownload.setOnClickListener {
            hideKeyboard()
            runLoadLatestArticlesAsync()
        }

        searchBox.afterTextChanged {
            internalSearchArticles(it)
        }

        btnImgRevert.setOnClickListener {
            hideKeyboard()
            searchBox.setText("")
            setupArticleAdapter()
        }

        searchBox.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })

    }

    private fun internalSearchArticles(searchTerm: String) {
        if (lockedListOfArticles.isNullOrEmpty()) return
        val temp = lockedListOfArticles?.toMutableList()
        temp?.let { itTemp ->
            filteredList = itTemp.filter {
                it.title.contains(searchTerm, ignoreCase = true) ||
                        it.body.contains(searchTerm, ignoreCase = true) ||
                        it.description.contains(searchTerm, ignoreCase = true) ||
                        it.published_date?.contains(searchTerm, ignoreCase = true) ?: false
            } as MutableList<Article>
        }

        setupArticleAdapter(filteredList.toMutableList())
    }

    private fun externalSearchArticlesAsync() {
        //make search!
        val searchTerm = searchBox.text.toString()
        if (!searchTerm.isNullOrEmpty()) {
            toggleLoading(on = true)
            io {
                val response = GmtHttpRequest().searchAsync(searchTerm).await()
                val arts = ArticleParser(response)
                lockedListOfArticles = arts.toList()
                main {
                    txtOverallArticleCount.text = "${lockedListOfArticles?.size} Total Articles"
                    setupArticleAdapter()
                    println(arts)
                }
            }
            searchBox.setText("")
            return
        }
    }

    private fun setLockedArticlesFromDB(response: com.github.kittinunf.fuel.core.Response) {
        val arts = ArticleParser(response)
        lockedListOfArticles = arts.toList()
        txtOverallArticleCount.text = "${lockedListOfArticles?.size} Total Articles"
    }

    private fun runLoadLatestArticlesAsync() {
        toggleLoading(true)
        io { loadLatestArticles() }
    }

    private fun toggleLoading(on:Boolean) {
        main {
            if (on) {
                loadingProgressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            } else {
                loadingProgressBar.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun loadLatestArticles() {
        val response = GmtHttpRequest().getAsync(GmtHttpRequest.URL_ARTICLES_DATA).await()
        main {
            setLockedArticlesFromDB(response)
            recyclerView.visibility = View.VISIBLE
            setupArticleAdapter() }
    }

    private fun setupArticleAdapter(articles: MutableList<Article>? = lockedListOfArticles?.toMutableList()) {

        articles?.let {
            if (it.isNotEmpty() && it.size > 0) {
                articleAdapter = null
                // only 100
                val lastIndex = it.indices.last
                val onlyOneHundredList = it.subList(0, if (lastIndex > 500) 500 else lastIndex )
                articleAdapter = recyclerView.initArticles(onlyOneHundredList)
                txtArticleCount.text = "${onlyOneHundredList.size} in list"
                toggleLoading(on = false)
                return
            }
        }
//        toast("No Articles!", MainGlewMeTvActivity.context)
    }

}

