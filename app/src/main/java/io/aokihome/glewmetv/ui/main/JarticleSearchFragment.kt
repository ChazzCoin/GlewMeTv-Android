package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.*
import io.aokihome.glewmetv.http.GmtHttpRequest
import io.aokihome.glewmetv.ui.adapters.ArticleListAdapter
import io.aokihome.glewmetv.utils.*
import kotlinx.android.synthetic.main.fragment_jarticle.*
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
        return inflater.inflate(R.layout.fragment_jarticle, container, false)
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
            if (it.length < 2) return@afterTextChanged
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
        filteredList = temp.search(searchTerm)
        setupArticleAdapter(filteredList.toMutableList())
    }

    private fun externalSearchArticlesAsync() {
        //make search!
        val searchTerm = searchBox.text.toString()
        if (!searchTerm.isNullOrEmpty()) {
            toggleLoading(on = true)
            clearRecyclerView()
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
            setupArticleAdapter()
        }
    }

    private fun clearRecyclerView() {
        lockedListOfArticles = null
        val emptyList = mutableListOf<Article>()
        recyclerView.initArticles(emptyList, fragmentActivity = this.requireActivity())
        txtOverallArticleCount.text = "0 Total Articles"
        txtArticleCount.text = "0 in list"
        recyclerView.visibility = View.INVISIBLE
    }

    private fun setupArticleAdapter(articles: MutableList<Article>? = lockedListOfArticles?.toMutableList()) {

        articles?.let {
            if (it.isNotEmpty() && it.size > 0) {
                articleAdapter = null
                // only 100
//                val lastIndex = it.indices.last
//                val onlyOneHundredList = it.subList(0, if (lastIndex > 500) 500 else lastIndex )
                it.shuffle()
                articleAdapter = recyclerView.initArticles(it, fragmentActivity = this.requireActivity())
                txtArticleCount.text = "${it.size} in list"
                toggleLoading(on = false)
                return
            }
        }
//        toast("No Articles!", MainGlewMeTvActivity.context)
    }

}

