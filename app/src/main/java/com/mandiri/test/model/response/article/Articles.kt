package com.mandiri.test.model.response.article

data class Articles(
    val status: String?,
    val totalResults: Int,
    val articles: ArrayList<Article>?
)