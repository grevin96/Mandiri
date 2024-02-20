package com.mandiri.test.view.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mandiri.test.api.ApiClient
import com.mandiri.test.model.response.article.Articles
import retrofit2.Call

class ArticleViewModel: ViewModel() {
    private var articleLiveData                 = MutableLiveData<Articles>()
    private var progressLiveData                = MutableLiveData<Boolean>()
    private var failureLiveData                 = MutableLiveData<Boolean>()
    private var callArticle: Call<Articles>?    = null

    fun observerArticle(): LiveData<Articles>   = articleLiveData
    fun observerProgress(): LiveData<Boolean>   = progressLiveData
    fun observerFailure(): LiveData<Boolean>    = failureLiveData

    fun getArticles(sources: String, article: String, page: Int) {
        callArticle             = ApiClient.api.getArticles(ApiClient.API_KEY, sources, article, page)
        progressLiveData.value  = true
        failureLiveData.value   = false

        callArticle?.enqueue(object: retrofit2.Callback<Articles> {
            override fun onResponse(call: Call<Articles>, response: retrofit2.Response<Articles>) {
                progressLiveData.value  = false

                if (response.body() != null) articleLiveData.value = response.body()!!
            }

            override fun onFailure(call: Call<Articles>, t: Throwable) {
                if (!call.isCanceled) failureLiveData.value = true
            }
        })
    }

    fun cancel() { callArticle?.cancel() }
}