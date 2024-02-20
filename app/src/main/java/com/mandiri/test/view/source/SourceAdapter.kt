package com.mandiri.test.view.source

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mandiri.test.databinding.ItemCategorySourceBinding
import com.mandiri.test.model.response.source.Source
import com.mandiri.test.utils.Utils
import com.mandiri.test.view.article.ArticleActivity

@SuppressLint("NotifyDataSetChanged")
class SourceAdapter(private val context: Context, val category: String): RecyclerView.Adapter<SourceAdapter.MyHolder>() {
    private lateinit var sources: ArrayList<Source>

    inner class MyHolder(val binding: ItemCategorySourceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(ItemCategorySourceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                            = sources.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder) {
            with(binding) {
                label.text          = sources[position].name
                divider.visibility  = if (position == itemCount - 1) View.GONE else View.VISIBLE

                itemView.setPadding(0, if (position == 0) Utils.gap(context, 4) else 0, 0, if (position == itemCount - 1) Utils.gap(context, 4) else 0)
                itemView.setOnClickListener {
                    val intent = Intent(context, ArticleActivity::class.java)

                    intent.putExtra("source", Gson().toJson(sources[position]))
                    intent.putExtra("category", category)
                    (context as Activity).startActivity(intent)
                }
            }
        }
    }

    fun data(sources: ArrayList<Source>) {
        this.sources = sources

        notifyDataSetChanged()
    }
}