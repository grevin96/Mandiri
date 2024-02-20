package com.mandiri.test.view.category

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mandiri.test.databinding.ItemCategorySourceBinding
import com.mandiri.test.utils.Utils
import com.mandiri.test.view.source.SourceActivity

@SuppressLint("NotifyDataSetChanged")
class CategoryAdapter(private val context: Context): RecyclerView.Adapter<CategoryAdapter.MyHolder>() {
    private lateinit var categories: ArrayList<String>

    inner class MyHolder(val binding: ItemCategorySourceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder = MyHolder(ItemCategorySourceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount(): Int                                            = categories.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder) {
            with(binding) {
                label.text          = categories[position]
                divider.visibility  = if (position == itemCount - 1) View.GONE else View.VISIBLE

                itemView.setPadding(0, if (position == 0) Utils.gap(context, 4) else 0, 0, if (position == itemCount - 1) Utils.gap(context, 4) else 0)
                itemView.setOnClickListener {
                    val intent = Intent(context, SourceActivity::class.java)

                    intent.putExtra("category", categories[position])
                    (context as Activity).startActivity(intent)
                }
            }
        }
    }

    fun data(categories: ArrayList<String>) {
        this.categories = categories

        notifyDataSetChanged()
    }
}