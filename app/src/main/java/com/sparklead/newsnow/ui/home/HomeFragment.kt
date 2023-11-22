package com.sparklead.newsnow.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import com.sparklead.newsnow.databinding.FragmentHomeBinding
import com.sparklead.newsnow.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.getAllNewsList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.homeUiState.collect {
                when(it) {
                    is HomeUiState.Empty -> {}
                    is HomeUiState.Error -> {
                        onError(it.message)
                    }
                    is HomeUiState.Loading -> {}
                    is HomeUiState.SuccessNewsList -> {
                        onSuccessNewsList(it.newsList)
                    }
                }
            }
        }
    }

    private fun onSuccessNewsList(newsList: List<Article>) {
        Log.e("@@@",newsList.toString())
    }

    private fun onError(message : String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}