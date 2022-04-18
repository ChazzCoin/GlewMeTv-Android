package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    var mainSession: Session? = null

    var articleAdapter: ArticleListAdapter? = null
    private var currentListOfArticles: List<Article>? = null
    var filteredList = mutableListOf<Article>()
    val io = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jarticle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainSession = Session.session
        if (!currentListOfArticles.isNullOrEmpty()){
            setupArticleAdapter()
        } else if (!mainSession?.cacheArticles.isNullOrEmpty()) {
            val temp = mainSession?.cacheArticles
            setupArticleAdapter(articles = temp)
//            toast("Cached Articles Loaded!")
        }

        // WORKING! SEARCH!
        btnSearchIcon.setOnClickListener {
            hideKeyboard()
            this.externalSearchArticlesAsync()
        }
        btnImgDownload.setOnClickListener {
            hideKeyboard()
            runLoadLatestArticlesAsync()
        }

        // Real-Time Search
//        searchBox.afterTextChanged {
//            if (it.isEmpty()) return@afterTextChanged
//            internalSearchArticles(it)
//        }

        btnImgRevert.setOnClickListener {
            hideKeyboard()
            searchBox.setText("")
            clearArticleAdapter()
        }

        btnImgLoadFavorites.setOnClickListener {
            hideKeyboard()
            val temp = mainSession?.savedArticles
            setupArticleAdapter(temp)
        }

        btnImgLoadCached.setOnClickListener {
            hideKeyboard()
            val temp = mainSession?.cacheArticles
            setupArticleAdapter(temp)
        }

        btnImgClearCache.setOnClickListener {
            hideKeyboard()
            clearArticleCache()
            showSuccess("Successfully Cleared Saved Cache.", context = context)
            setupArticleAdapter()
        }

        btnImgClearCache.setOnLongClickListener {
            hideKeyboard()
            clearArticleFavorites()
            showSuccess("Successfully Cleared Favorites.", context = context)
            return@setOnLongClickListener true
        }

        btnImgSaveCache.setOnClickListener {
            hideKeyboard()
            articleAdapter?.listOfArticles?.saveCached()
            showSuccess("Successfully Saved Current List to Cache.", context = context)
        }

        searchBox.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                hideKeyboard()
                internalSearchArticles(searchBox.text.toString())
                searchBox.setText("")
                return@OnKeyListener true
            }
            false
        })

    }

    private fun internalSearchArticles(searchTerm: String, articleList: MutableList<Article>? = currentListOfArticles?.toMutableList()) {
        if (articleList.isNullOrEmpty()) return
        filteredList = articleList.search(searchTerm)
        setupSearchArticleAdapter(filteredList.toMutableList())
    }

    private fun externalSearchArticlesAsync() {
        //make search!
        val searchTerm = searchBox.text.toString()
        if (!searchTerm.isNullOrEmpty()) {
            toggleLoading(on = true)
            clearRecyclerView()
            showSuccess("Searching Hark Database for: [ $searchTerm ]", context = context)
            io {
                val response = GmtHttpRequest().searchAsync(searchTerm).await()
                val arts = ArticleParser(response)
                currentListOfArticles = arts.toList()
                main {
                    txtOverallArticleCount.text = "${currentListOfArticles?.size} Total Articles"
                    setupArticleAdapter()
                    println(arts)
                }
            }
            searchBox.setText("")
            return
        }
    }


    private fun setCurrentArticlesFromDB(response: com.github.kittinunf.fuel.core.Response) {
        val arts = ArticleParser(response)
        currentListOfArticles = null
        currentListOfArticles = arts.toList()
        currentListOfArticles?.saveCached()
        txtOverallArticleCount.text = "${currentListOfArticles?.size} Total Articles"
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
            setCurrentArticlesFromDB(response)
            setupArticleAdapter()
        }
    }

    private fun clearRecyclerView() {
        currentListOfArticles = null
        val emptyList = mutableListOf<Article>()
        recyclerView.initArticles(emptyList, fragmentActivity = this.requireActivity())
        txtOverallArticleCount.text = "0 Total Articles"
        txtArticleCount.text = "0 in list"
        recyclerView.visibility = View.INVISIBLE
    }

    private fun setupArticleAdapter(articles: MutableList<Article>? = currentListOfArticles?.toMutableList()) {

        articles?.let { itArticles ->
            if (itArticles.isNotEmpty() && itArticles.size > 0) {
                articleAdapter = null
                currentListOfArticles = itArticles
                realm().executeTransaction {
                    itArticles.shuffle()
                }
                articleAdapter = recyclerView.initArticles(itArticles, fragmentActivity = this.requireActivity())
                txtArticleCount.text = "${itArticles.size} in list"
                toggleLoading(on = false)
                return
            }
        }
        articleAdapter = null
        articleAdapter = recyclerView.initArticles(mutableListOf(), fragmentActivity = this.requireActivity())
        txtArticleCount.text = "0 in list"
        toggleLoading(on = false)
    }

    private fun clearArticleAdapter() {
        articleAdapter = null
        articleAdapter = recyclerView.initArticles(mutableListOf(), fragmentActivity = this.requireActivity())
        txtArticleCount.text = "0 in list"
        toggleLoading(on = false)
    }

    private fun setupSearchArticleAdapter(articles: MutableList<Article>? = currentListOfArticles?.toMutableList()) {

        articles?.let { itArticles ->
            if (itArticles.isNotEmpty() && itArticles.size > 0) {
                articleAdapter = null
                realm().executeTransaction {
                    itArticles.shuffle()
                }
                articleAdapter = recyclerView.initArticles(itArticles, fragmentActivity = this.requireActivity())
                txtArticleCount.text = "${itArticles.size} in list"
                toggleLoading(on = false)
                return
            }
        }
        articleAdapter = null
        articleAdapter = recyclerView.initArticles(mutableListOf(), fragmentActivity = this.requireActivity())
        txtArticleCount.text = "0 in list"
        toggleLoading(on = false)
    }


}

