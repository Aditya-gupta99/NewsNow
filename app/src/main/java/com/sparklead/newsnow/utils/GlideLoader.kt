package com.sparklead.newsnow.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sparklead.newsnow.R
import java.io.IOException

class GlideLoader(private val context: Context) {

    // load news images
    fun loadNewsPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .timeout(30000)
                .placeholder(R.drawable.news_now_logo)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}