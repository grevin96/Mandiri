package com.mandiri.test.view.article

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mandiri.test.databinding.ItemArticleBinding
import com.mandiri.test.databinding.ItemLoadingBinding
import com.mandiri.test.model.response.article.Article
import com.mandiri.test.utils.Utils
import com.mandiri.test.view.web.WebActivity

@SuppressLint("NotifyDataSetChanged")
class ArticleAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var articles = ArrayList<Article?>()

    inner class MyHolder(val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root)
    inner class LoadingHolder(val binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  = if (viewType == 0) LoadingHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)) else MyHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                                            = articles.size

    override fun getItemViewType(position: Int): Int {
        return if (articles[position] == null) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            with(holder as MyHolder) {
                with(binding) {
                    title.text          = articles[position]!!.title
                    description.text    = articles[position]!!.description
                    time.text           = articles[position]!!.publishedAt!!.substring(0, 10)

                    Glide.with(context).load(articles[position]!!.urlToImage).into(image)
                    itemView.setPadding(Utils.gap(context, 16), if (position == 0) Utils.gap(context, 16) else Utils.gap(context, 12), Utils.gap(context, 16), if (position == itemCount - 1) Utils.gap(context, 16) else Utils.gap(context, 12))
                    itemView.setOnClickListener {
                        val intent = Intent(context, WebActivity::class.java)

                        intent.putExtra("url", articles[position]!!.url)
                        (context as Activity).startActivity(intent)
                    }
                }
            }
        }
    }

    fun data(articles: ArrayList<Article?>) {
        this.articles = articles

        notifyDataSetChanged()
    }
}