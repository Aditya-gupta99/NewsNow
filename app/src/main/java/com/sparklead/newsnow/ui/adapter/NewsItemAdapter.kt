package com.sparklead.newsnow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sparklead.newsnow.databinding.ItemNewsBinding
import com.sparklead.newsnow.model.Article

class NewsItemAdapter : RecyclerView.Adapter<NewsItemAdapter.NewsViewHolder>() {

    var onItemClick: ((Article) -> Unit)? = null

    inner class NewsViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        with(holder) {
            with(differ.currentList[position]) {
                binding.tvTitle.text = this.title
                binding.cvNews.setOnClickListener {
                    onItemClick?.invoke(this)
                }
            }
            this.setIsRecyclable(false)
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}