package com.sparklead.newsnow.model

data class NewsResponse(
    val articles: List<Article>,

    val status: String
)