package com.sparklead.newsnow.ui.detail

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.sparklead.newsnow.databinding.FragmentNewsDetailBinding

class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
            WebSettingsCompat.setAlgorithmicDarkeningAllowed(binding.wbNews.settings, true)
        }

        binding.pbLoading.max = 10

        ObjectAnimator.ofInt(binding.pbLoading, "progress", 10)
            .setDuration(3000)
            .start()

        binding.wbNews.apply {
            webViewClient = WebViewClient()
            loadUrl(args.article.url)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            binding.pbLoading.visibility = View.GONE
        }, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}