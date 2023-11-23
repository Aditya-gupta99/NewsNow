package com.sparklead.newsnow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sparklead.newsnow.databinding.FragmentHomeBinding
import com.sparklead.newsnow.databinding.ItemNewsBinding
import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.utils.GlideLoader

class NewsItemAdapter : RecyclerView.Adapter<NewsItemAdapter.NewsViewHolder>() {

    // onclick if user select any article
    var onItemClick: ((Article) -> Unit)? = null

    inner class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    private var newsList = listOf<Article>()

    fun submitList(list: List<Article>) {
        newsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        with(holder) {
            with(newsList[position]) {
                binding.tvTitle.text = this.title
                binding.cvNews.setOnClickListener {
                    onItemClick?.invoke(this)
                }
                GlideLoader(itemView.context).loadNewsPicture(this.urlToImage, binding.ivNews)
                binding.tvAuthor.text = this.author
                val date = this.publishedAt.split("T")
                binding.tvTime.text = date[0] + "   " + date[1].subSequence(0,date[1].length-1)
            }
            this.setIsRecyclable(false)
        }

    }
}