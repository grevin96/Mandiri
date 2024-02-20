package com.mandiri.test.view.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mandiri.test.R
import com.mandiri.test.databinding.ActivityWebBinding

@SuppressLint("SetJavaScriptEnabled")
class WebActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebBinding.inflate(layoutInflater)

        setContentView(binding.root)
        bundle()
        toolbar()
        web()
    }

    private fun bundle() {
        val bundle  = intent.extras
        url         = bundle?.getString("url").toString()
    }

    private fun toolbar() {
        with(binding) {
            with(toolbar) {
                title.text          = resources.getString(R.string.article_details)
                search.visibility   = View.GONE

                back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }
        }
    }

    private fun web() {
        with(binding) {
            web.settings.javaScriptEnabled  = true
            web.webViewClient               = WebViewClient()

            web.loadUrl(url)
        }
    }
}