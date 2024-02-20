package com.mandiri.test.view.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mandiri.test.api.ApiClient
import com.mandiri.test.model.response.source.Source
import com.mandiri.test.model.response.source.Sources
import retrofit2.Call

class SourceViewModel: ViewModel() {
    private var sourcesLiveData             = MutableLiveData<ArrayList<Source>>()
    private var progressLiveData            = MutableLiveData<Boolean>()
    private var failureLiveData             = MutableLiveData<Boolean>()
    private var callSources: Call<Sources>? = null

    fun observerSources(): LiveData<ArrayList<Source>>  = sourcesLiveData
    fun observerProgress(): LiveData<Boolean>           = progressLiveData
    fun observerFailure(): LiveData<Boolean>            = failureLiveData

    fun getSources(category: String) {
        callSources             = ApiClient.api.getSources(ApiClient.API_KEY, category)
        progressLiveData.value  = true
        failureLiveData.value   = false

        callSources?.enqueue(object: retrofit2.Callback<Sources> {
            override fun onResponse(call: Call<Sources>, response: retrofit2.Response<Sources>) {
                progressLiveData.value  = false

                if (response.body() != null) sourcesLiveData.value = response.body()!!.sources
            }

            override fun onFailure(call: Call<Sources>, t: Throwable) {
                if (!call.isCanceled) failureLiveData.value = true
            }
        })
    }

    fun cancel() { callSources?.cancel() }
}