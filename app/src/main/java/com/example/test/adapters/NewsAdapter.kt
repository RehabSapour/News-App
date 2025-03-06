package com.example.test.adapters

import android.content.Context
import android.provider.MediaStore.Audio.Artists
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.api.datamodel.newsItem
import com.squareup.picasso.Picasso


interface OnItemClickListener {
    fun onItemClick(url: String)
}

class NewsAdapter(private val context: Context, private var itemClickListener: OnItemClickListener) :
    ListAdapter<newsItem, NewsAdapter.NewsViewHolder>(NewsItemDiffCallback()) {

     var newsList: List<newsItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return NewsViewHolder(view,itemClickListener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount(): Int = newsList.size

    class NewsViewHolder(itemView: View, private var itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val channelNameTextView: TextView = itemView.findViewById(R.id.channel_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val timeTextView: TextView = itemView.findViewById(R.id.time)
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsimg)
       // private var itemClickListener: OnItemClickListener? = null
        fun bind(newsItem: newsItem) {
            // Bind the data to the views
            channelNameTextView.text = newsItem.source.name
            descriptionTextView.text = newsItem.description
            timeTextView.text = newsItem.publishedAt
            val iconUrl =newsItem.urlToImage
            Picasso.get().load(iconUrl).into(newsImageView)
            itemView.setOnClickListener {
                itemClickListener.onItemClick(newsItem.url)
            }
        }
    }

    fun updateArticles(list: List<newsItem>){
           newsList = list
        Log.d("ArticlesUpdated", "Updated adapter with ${newsList.size} articles")
        notifyDataSetChanged()
    }
    // DiffUtil callback for efficient updates
    class NewsItemDiffCallback : DiffUtil.ItemCallback<newsItem>() {
        override fun areItemsTheSame(oldItem: newsItem, newItem: newsItem): Boolean {
            return oldItem.url == newItem.url // Assuming URL is unique
        }

        override fun areContentsTheSame(oldItem: newsItem, newItem: newsItem): Boolean {
            return oldItem == newItem // Compare the entire item content
        }
    }

}

