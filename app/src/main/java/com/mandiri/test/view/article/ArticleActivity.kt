package com.mandiri.test.view.article

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mandiri.test.databinding.ActivityArticleBinding
import com.mandiri.test.model.response.article.Article
import com.mandiri.test.model.response.source.Source

@SuppressLint("SetTextI18n", "NotifyDataSetChanged")
class ArticleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var viewModel: ArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var source: Source
    private lateinit var category: String

    private var articles    = ArrayList<Article?>()
    private var totalPage   = 0
    private var page        = 1
    private var isLoadMore  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleBinding.inflate(layoutInflater)

        setContentView(binding.root)
        bundle()
        toolbar()
        swipeRefresh()
        articleAdapter()
        viewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancel()
    }

    private fun bundle() {
        val bundle  = intent.extras
        source      = Gson().fromJson(bundle?.getString("source"), Source::class.java)
        category    = bundle?.getString("category").toString()
    }

    private fun toolbar() {
        with(binding) {
            with(toolbar) {
                title.text      = category + " / " + source.name
                val handler     = Handler(Looper.getMainLooper())
                val runnable    = Runnable { refresh() }

                back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
                search.addTextChangedListener {
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 1200)
                }
            }
        }
    }

    private fun swipeRefresh() {
        binding.refresh.setColorSchemeColors(Color.RED)
        binding.refresh.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        page = 1

        request()
    }

    private fun viewModel() {
        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]

        request()
        viewModel.observerArticle().observe(this) { articles ->
            run {
                totalPage = articles.totalResults / 20

                if (page == 1 && articles.articles!!.size > 0) this@ArticleActivity.articles.clear()
                articles.articles?.let { this@ArticleActivity.articles.addAll(it) }

                if (page == 1) articleAdapter.data(this@ArticleActivity.articles)
                articleAdapter.notifyDataSetChanged()
            }
        }
        viewModel.observerProgress().observe(this) { flag -> progress(flag) }
        viewModel.observerFailure().observe(this) { flag -> failure(flag) }
    }

    private fun progress(flag: Boolean) {
        with(binding) {
            if (refresh.isRefreshing) {
                if (!flag) refresh.isRefreshing = false
            } else {
                if (page == 1) {
                    loading.visibility  = if (flag) View.VISIBLE else View.GONE
                    recycler.visibility = if (!flag) View.VISIBLE else View.GONE
                } else {
                    if (flag) {
                        if (articles[articles.size - 1] != null) articles.add(null)
                    }
                    else articles.removeAt(articles.size - 1)
                    articleAdapter.notifyDataSetChanged()
                }

                if (!flag) isLoadMore = true
            }
        }
    }

    private fun failure(flag: Boolean) {
        if (flag) request()
    }

    private fun articleAdapter() {
        articleAdapter = ArticleAdapter(this)

        binding.recycler.apply {
            linearLayoutManager = LinearLayoutManager(context)
            layoutManager       = linearLayoutManager
            adapter             = articleAdapter
        }
        
        binding.recycler.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isLoadMore && linearLayoutManager.findLastVisibleItemPosition() > articles.size - 2) {
                    page++
                    if (page <= totalPage) {
                        isLoadMore = false

                        request()
                    }
                }
            }
        })
    }

    private fun request() {
        viewModel.cancel()
        source.id?.let { viewModel.getArticles(it, binding.toolbar.search.text.toString(), page) }
    }
}