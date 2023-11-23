package com.sparklead.newsnow.ui.home

import com.sparklead.newsnow.model.Article

sealed class HomeUiState {

    data object Empty : HomeUiState()

    data object Loading : HomeUiState()

    data class Error(val message: String) : HomeUiState()

    data class SuccessNewsList(val newsList: List<Article>) : HomeUiState()
}
