package com.example.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NewsChannelFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_news_channel, container, false)
    }
    companion object {
        fun newInstance(channelId: Int): NewsChannelFragment {
            val fragment = NewsChannelFragment()
            // Pass arguments to fetch news for the specified channel
            val args = Bundle()
            args.putInt("channelId", channelId)
            fragment.arguments = args
            return fragment
        }
    }
}