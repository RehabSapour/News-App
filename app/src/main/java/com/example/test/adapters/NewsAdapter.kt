package com.example.test.adapters
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.api.datamodel.newsItem
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale




class NewsAdapter(private val context: Context) :
    ListAdapter<newsItem, NewsAdapter.NewsViewHolder>(NewsItemDiffCallback()) {
     var newsList: List<newsItem> = listOf()  // list of Articles

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        holder.bind(newsItem)
    }

    override fun getItemCount(): Int = newsList.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val channelNameTextView: TextView = itemView.findViewById(R.id.channel_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val timeTextView: TextView = itemView.findViewById(R.id.time)
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsimg)
        private val seeMore: TextView = itemView.findViewById(R.id.see_more)
        private val sharedPreferences = itemView.context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        private val fontSize = sharedPreferences.getInt("font_size", 16)

        fun bind(newsItem: newsItem) {
           // Format the publishedAt timestamp
            channelNameTextView.textSize=fontSize.toFloat()
            descriptionTextView.textSize=(fontSize-2).toFloat()
            timeTextView.textSize=(fontSize-4).toFloat()
           val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
           val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())

           try {
               val date = inputFormat.parse(newsItem.publishedAt) // Parse the input date string
               val formattedDate = outputFormat.format(date!!) // Format the date to desired output
               timeTextView.text = formattedDate
           } catch (e: ParseException) {
               e.printStackTrace()
               timeTextView.text = newsItem.publishedAt // Fallback to original format if error
           }

            // Bind the data to the views
            channelNameTextView.text = newsItem.source.name
            descriptionTextView.text = newsItem.description
            val iconUrl =newsItem.urlToImage
            Picasso.get().load(iconUrl).into(newsImageView)


           itemView.setOnClickListener {
               val bundle = Bundle().apply {
                   putString("url", newsItem.url)
               }
               Navigation.findNavController(itemView).navigate(R.id.webViewFragment, bundle)
             }
           seeMore.setOnClickListener {
               val bundle = Bundle().apply {
                   putString("url", newsItem.url)
               }
               Navigation.findNavController(itemView).navigate(R.id.webViewFragment, bundle)
           }

           itemView.setOnLongClickListener {
               showShareDialog(newsItem.url)
               true
           }
        }

        private fun showShareDialog(newsUrl: String) {
            val shareIntend = Intent(Intent.ACTION_SEND)
            shareIntend.type = "text/plain" // type of message to send
            shareIntend.putExtra(Intent.EXTRA_TEXT, newsUrl)
            itemView.context.startActivity(Intent.createChooser(shareIntend, ""))
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

