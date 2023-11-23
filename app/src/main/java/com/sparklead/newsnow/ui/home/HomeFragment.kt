package com.sparklead.newsnow.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.sparklead.newsnow.R
import com.sparklead.newsnow.databinding.FragmentHomeBinding
import com.sparklead.newsnow.firebase.FirebaseService
import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.model.NotificationData
import com.sparklead.newsnow.model.PushNotification
import com.sparklead.newsnow.ui.adapter.NewsItemAdapter
import com.sparklead.newsnow.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val newsItemAdapter by lazy { NewsItemAdapter() }
    private var token = ""
    private var checkedFilter = 0
    private var newsListSuccess = listOf<Article>()
    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        FirebaseService.sharedPref =
            requireContext().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Fcm token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
            FirebaseService.token = token
        })

        FirebaseMessaging.getInstance().subscribeToTopic("/topic/$token")

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

        binding.fabNotification.setOnClickListener {
            sendNotification()
        }

        binding.filterSelectionButton.setOnClickListener {
            showFilterDialog()
        }

        binding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newsFilter(text)
                }, 300)
                return true
            }

            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }
        })
    }

    private fun newsFilter(text: String?) {
        val filterNewsList = ArrayList<Article>()
        for (it in newsListSuccess) {
            if (it.title.contains(text ?: "", true)) {
                filterNewsList.add(it)
            }
        }
        if (filterNewsList.size == 0) Toast.makeText(
            requireContext(),
            "No Article found",
            Toast.LENGTH_SHORT
        ).show()
        newsItemAdapter.submitList(filterNewsList)
    }

    private fun sendNotification() {
        val topic = "/topic/$token"
        PushNotification(
            NotificationData("NewsNow", "Hi this is demo notification", "demo"),
            topic
        ).also {
            viewModel.sendNotification(it)
        }
        Toast.makeText(requireContext(), "Notification fab has been pressed", Toast.LENGTH_SHORT)
            .show()
    }

    private fun onSuccessNewsList(newsList: List<Article>) {
        newsItemAdapter.submitList(newsList)
        newsListSuccess = newsList
    }

    private fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showFilterDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setSingleChoiceItems(
            R.array.search_options,
            checkedFilter
        ) { dialog, index ->
            checkedFilter = index
            onClickSearch(index)
            binding.filterSelectionButton.text =
                resources.getStringArray(R.array.search_options)[index]
            dialog.dismiss()
        }
        dialogBuilder.show()
    }

    private fun onClickSearch(index: Int) {
        when (index) {
            0 -> {
                sortNormal()
            }

            1 -> {
                sortOldToNew()
            }

            2 -> {
                sortNewToOld()
            }

            3 -> {
                sortAToZ()
            }

            4 -> {
                sortZToA()
            }
        }
    }

    private fun sortNormal() {
        newsItemAdapter.submitList(newsListSuccess)
    }

    private fun sortAToZ() {
        newsItemAdapter.submitList(newsListSuccess.sortedBy { it.title })
    }

    private fun sortOldToNew() {
        newsItemAdapter.submitList(newsListSuccess.sortedBy { it.publishedAt })
    }

    private fun sortNewToOld() {
        newsItemAdapter.submitList(newsListSuccess.sortedByDescending { it.publishedAt })
    }

    private fun sortZToA() {
        newsItemAdapter.submitList(newsListSuccess.sortedByDescending { it.title })
    }
}