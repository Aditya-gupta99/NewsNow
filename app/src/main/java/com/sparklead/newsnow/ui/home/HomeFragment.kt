package com.sparklead.newsnow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sparklead.newsnow.databinding.FragmentHomeBinding
import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.ui.adapter.NewsItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val newsItemAdapter by lazy { NewsItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.getAllNewsList()
        setUpAdapter()
        return binding.root
    }

    private fun setUpAdapter() {
        binding.apply {
            rvNewsList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = newsItemAdapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.homeUiState.collect {
                when (it) {
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

        newsItemAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun onSuccessNewsList(newsList: List<Article>) {
        newsItemAdapter.differ.submitList(newsList)
    }

    private fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}