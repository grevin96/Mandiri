package com.mandiri.test.view.source

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mandiri.test.databinding.ActivitySourceBinding
import com.mandiri.test.model.response.source.Source

class SourceActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySourceBinding
    private lateinit var viewModel: SourceViewModel
    private lateinit var sourceAdapter: SourceAdapter
    private lateinit var category: String

    private var sources = ArrayList<Source>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySourceBinding.inflate(layoutInflater)

        setContentView(binding.root)
        bundle()
        toolbar()
        sourceAdapter()
        viewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancel()
    }

    private fun bundle() {
        val bundle  = intent.extras
        category    = bundle?.getString("category").toString()
    }

    private fun toolbar() {
        with(binding) {
            with(toolbar) {
                title.text = category

                back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
                search.addTextChangedListener {
                    if (sources.size > 0) {
                        val newSources = ArrayList<Source>()

                        for (source in sources) {
                            if (source.name!!.lowercase().contains(it.toString().lowercase())) newSources.add(source)
                        }

                        sourceAdapter.data(newSources)
                    }
                }
            }
        }
    }

    private fun viewModel() {
        viewModel = ViewModelProvider(this)[SourceViewModel::class.java]

        request()
        viewModel.observerSources().observe(this) { sources ->
            run {
                this@SourceActivity.sources = sources

                sourceAdapter.data(sources)
            }
        }
        viewModel.observerProgress().observe(this) { flag -> progress(flag) }
        viewModel.observerFailure().observe(this) { flag -> failure(flag) }
    }

    private fun progress(flag: Boolean) {
        with(binding) {
            loading.visibility  = if (flag) View.VISIBLE else View.GONE
            recycler.visibility = if (!flag) View.VISIBLE else View.GONE
        }
    }

    private fun failure(flag: Boolean) {
        if (flag) request()
    }

    private fun sourceAdapter() {
        sourceAdapter = SourceAdapter(this, category)

        binding.recycler.apply {
            layoutManager   = LinearLayoutManager(context)
            adapter         = sourceAdapter
        }
    }

    private fun request() {
        viewModel.cancel()
        viewModel.getSources(category.lowercase())
    }
}