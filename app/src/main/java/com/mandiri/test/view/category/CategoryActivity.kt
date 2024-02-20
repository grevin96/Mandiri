package com.mandiri.test.view.category

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mandiri.test.R
import com.mandiri.test.databinding.ActivityCategoryBinding
import com.mandiri.test.utils.Utils

class CategoryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)
        toolbar()
        categoryAdapter()
        addCategory()
    }

    private fun toolbar() {
        with(binding) {
            with(toolbar) {
                title.text          = resources.getString(R.string.category)
                back.visibility     = View.GONE
                search.visibility   = View.GONE

                title.setPadding(Utils.gap(this@CategoryActivity, 16), 0, 0, 0)
            }
        }
    }

    private fun categoryAdapter() {
        categoryAdapter = CategoryAdapter(this)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter       = categoryAdapter
        }
    }

    private fun addCategory() {
        val categories = ArrayList<String>()

        categories.add(resources.getString(R.string.business))
        categories.add(resources.getString(R.string.entertainment))
        categories.add(resources.getString(R.string.general))
        categories.add(resources.getString(R.string.health))
        categories.add(resources.getString(R.string.science))
        categories.add(resources.getString(R.string.sports))
        categories.add(resources.getString(R.string.technology))
        categoryAdapter.data(categories)
    }
}