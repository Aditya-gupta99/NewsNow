package com.sparklead.newsnow.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: NewsListRepository) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Empty)
    val homeUiState = _homeUiState

    fun getAllNewsList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNewsArticle().catch {
            _homeUiState.value = HomeUiState.Error(it.message.toString())
        }.collect {
            _homeUiState.value = HomeUiState.SuccessNewsList(it)
        }
    }

}